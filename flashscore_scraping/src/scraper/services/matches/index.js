import { openPageAndNavigate, waitForSelectorSafe } from "../../index.js";

export const getMatchLinks = async (context, leagueSeasonUrl, type) => {
  const page = await openPageAndNavigate(context, `${leagueSeasonUrl}/${type}`);
  

  // Bardziej stabilny fallback dla przycisku "Pokaż więcej meczów"
  const LOAD_MORE_SELECTOR = '[data-testid="wcl-buttonLink"], .event__more'; 
  // Szukamy elementów, których ID zaczyna się od "g_1_"
  const MATCH_SELECTOR = 'div[id^="g_1_"]'; 
  const CLICK_DELAY = 600;
  const MAX_EMPTY_CYCLES = 4;

  let emptyCycles = 0;

  while (true) {
    const countBefore = await page.$$eval(MATCH_SELECTOR, (els) => els.length);

    const loadMoreBtn = await page.$(LOAD_MORE_SELECTOR);
    if (!loadMoreBtn) break;

    try {
      // Zabezpieczenie: sprawdzamy czy przycisk jest faktycznie widoczny
      const isVisible = await loadMoreBtn.isVisible();
      if (!isVisible) break;

      await loadMoreBtn.scrollIntoViewIfNeeded();
      await loadMoreBtn.click();
      await page.waitForTimeout(CLICK_DELAY);
    } catch {
      break;
    }

    const countAfter = await page.$$eval(MATCH_SELECTOR, (els) => els.length);

    if (countAfter === countBefore) {
      emptyCycles++;
      if (emptyCycles >= MAX_EMPTY_CYCLES) break;
    } else {
      emptyCycles = 0;
    }
  }

  await waitForSelectorSafe(page, [MATCH_SELECTOR]);

  const matchIdList = await page.evaluate((selector) => {
    return Array.from(document.querySelectorAll(selector)).map((element) => {
      const id = element?.id?.replace("g_1_", "");
      // Samodzielnie budujemy stabilny URL na podstawie ID meczu
      const url = `https://www.flashscore.com/match/${id}/#/match-summary`;
      return { id, url };
    }).filter(match => match.id); // Odsiewamy ewentualne puste wyniki
  }, MATCH_SELECTOR);

  await page.close();

  console.info(`✅ Found ${matchIdList.length} matches for ${type}`);
  return matchIdList;
};

export const getMatchData = async (context, { id: matchId, url }) => {
  const page = await openPageAndNavigate(context, url);

  await waitForSelectorSafe(page, [
    ".duelParticipant__startTime",
    "div[data-testid='wcl-summaryMatchInformation'] > div",
  ]);

  await page.waitForFunction(() => {

    const homeName = document.querySelector(
        ".duelParticipant__home .participant__participantName.participant__overflow"
    );

    const awayName = document.querySelector(
        ".duelParticipant__away .participant__participantName.participant__overflow"
    );

    const homeLogo = document.querySelector(
        ".duelParticipant__home .participant__image"
    );

    const awayLogo = document.querySelector(
        ".duelParticipant__away .participant__image"
    );

    return (
        homeName &&
        awayName &&
        homeLogo &&
        awayLogo &&
        homeName.textContent.trim().length > 0 &&
        awayName.textContent.trim().length > 0 &&
        homeLogo.src.length > 0 &&
        awayLogo.src.length > 0
    );
}, { timeout: 10000 });

  await waitForMatchData(page);

  const matchData = await extractMatchData(page);
  const information = await extractMatchInformation(page);

  const statsLink = buildStatsUrl(url, matchId);
  await page.goto(statsLink, { waitUntil: "domcontentloaded" });

  await waitForSelectorSafe(page, [
    "div[data-testid='wcl-statistics']",
    "div[data-testid='wcl-statistics-value']",
  ]);

  const statistics = await extractMatchStatistics(page);

  await page.close();
  return { matchId, ...matchData, information, statistics };
};

const waitForMatchData = async (page) => {
  await page.waitForFunction(() => {
    const dateElement = document.querySelector(
      ".duelParticipant__startTime"
    );

    const statusElement = document.querySelector(
      ".fixedHeaderDuel__detailStatus"
    );

    const scoreElements = document.querySelectorAll(
      ".detailScore__wrapper span:not(.detailScore__divider)"
    );

    if (!dateElement) {
      return false;
    }

    const dateText = dateElement.innerText.trim();
    const statusText = statusElement?.innerText.trim() ?? "";

    const scores = Array.from(scoreElements)
      .map((element) => element.innerText.trim())
      .filter(Boolean);

    const [datePart, timePart] = dateText.split(" ");

    if (!datePart || !timePart) {
      return false;
    }

    const [day, month, year] = datePart.split(".").map(Number);
    const [hour, minute] = timePart.split(":").map(Number);

    const matchDate = new Date(
      year,
      month - 1,
      day,
      hour,
      minute
    );

    const now = new Date();

    // Mecz jeszcze się nie rozpoczął.
    // NOT STARTED + brak wyniku jest tutaj prawidłowe.
    if (matchDate > now) {
      return true;
    }

    // Mecz już powinien trwać lub być zakończony.
    // Jeżeli Flashscore podał status inny niż NOT STARTED,
    // uznajemy dane za wystarczające.
    if (statusText && statusText !== "NOT STARTED") {
      return true;
    }

    // Jeżeli dostępne są oba wyniki, dane są wystarczające.
    const hasScores =
      scores.length >= 2 &&
      scores[0] !== "" &&
      scores[1] !== "" &&
      !Number.isNaN(Number(scores[0])) &&
      !Number.isNaN(Number(scores[1]));

    if (hasScores) {
      return true;
    }

    return false;
  }, { timeout: 10000 });
};

const buildStatsUrl = (matchUrl, matchId) => {
  if (!matchUrl || !matchId) return null;

  const url = new URL(matchUrl);
  const base = url.origin + url.pathname.replace(/\/$/, "");

  return `${base}/summary/stats/0/?mid=${matchId}`;
};

const extractMatchData = async (page) => {
  await waitForSelectorSafe(page, [
    "span[data-testid='wcl-scores-overline-03']",
    ".duelParticipant__startTime",
    ".fixedHeaderDuel__detailStatus",
    ".tournamentHeader__country > a",
    ".detailScore__wrapper span:not(.detailScore__divider)",
    ".duelParticipant__home .participant__image",
    ".duelParticipant__away .participant__image",
    ".duelParticipant__home .participant__participantName.participant__overflow",
    ".duelParticipant__away .participant__participantName.participant__overflow",
  ]);

  return await page.evaluate(() => {
    return {
      stage: document
        .querySelector('meta[property="og:description"]')
        ?.getAttribute("content")
        ?.match(/ - (.+)$/)?.[1]
        ?.trim(),
      date: document
        .querySelector(".duelParticipant__startTime")
        ?.innerText.trim(),
      status:
        document
          .querySelector(".fixedHeaderDuel__detailStatus")
          ?.innerText.trim() ?? "NOT STARTED",
      home: {
        name: document
          .querySelector(
            ".duelParticipant__home .participant__participantName.participant__overflow"
          )
          ?.innerText.trim(),
        image: document.querySelector(
          ".duelParticipant__home .participant__image"
        )?.src,
      },
      away: {
        name: document
          .querySelector(
            ".duelParticipant__away .participant__participantName.participant__overflow"
          )
          ?.innerText.trim(),
        image: document.querySelector(
          ".duelParticipant__away .participant__image"
        )?.src,
      },
      result: {
        home: Array.from(
          document.querySelectorAll(
            ".detailScore__wrapper span:not(.detailScore__divider)"
          )
        )?.[0]?.innerText.trim(),
        away: Array.from(
          document.querySelectorAll(
            ".detailScore__wrapper span:not(.detailScore__divider)"
          )
        )?.[1]?.innerText.trim(),
        regulationTime: document
          .querySelector(".detailScore__fullTime")
          ?.innerText.trim()
          .replace(/[\n()]/g, ""),
        penalties: Array.from(
          document.querySelectorAll('[data-testid="wcl-scores-overline-02"]')
        )
          .find(
            (element) => element.innerText.trim().toLowerCase() === "penalties"
          )
          ?.nextElementSibling?.innerText?.trim()
          .replace(/\s+/g, ""),
      },
    };
  });
};

const extractMatchInformation = async (page) => {
  return await page.evaluate(async () => {
    const elements = Array.from(
      document.querySelectorAll(
        "div[data-testid='wcl-summaryMatchInformation'] > div"
      )
    );
    return elements.reduce((acc, element, index) => {
      if (index % 2 === 0) {
        acc.push({
          category: element?.textContent
            .trim()
            .replace(/\s+/g, " ")
            .replace(/(^[:\s]+|[:\s]+$|:)/g, ""),
          value: elements[index + 1]?.innerText
            .trim()
            .replace(/\s+/g, " ")
            .replace(/(^[:\s]+|[:\s]+$|:)/g, ""),
        });
      }
      return acc;
    }, []);
  });
};

const extractMatchStatistics = async (page) => {
  return await page.evaluate(async () => {
    return Array.from(
      document.querySelectorAll("div[data-testid='wcl-statistics']")
    ).map((element) => ({
      category: element
        .querySelector("div[data-testid='wcl-statistics-category']")
        ?.innerText.trim(),
      homeValue: Array.from(
        element.querySelectorAll(
          "div[data-testid='wcl-statistics-value'] > strong"
        )
      )?.[0]?.innerText.trim(),
      awayValue: Array.from(
        element.querySelectorAll(
          "div[data-testid='wcl-statistics-value'] > strong"
        )
      )?.[1]?.innerText.trim(),
    }));
  });
};
