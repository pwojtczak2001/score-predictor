import { TIMEOUT } from "../../../constants/index.js";
import { openPageAndNavigate, waitForSelectorSafe } from "../../index.js";

export const getListOfSeasons = async (context, leagueUrl) => {
  const page = await openPageAndNavigate(context, `${leagueUrl}/archive`);

  // console.log('archiveTable__column');
  await waitForSelectorSafe(page, [".archiveTable__column > a"], TIMEOUT);

  const listOfLeagueSeasons = await page.evaluate(() => {
    return Array.from(document.querySelectorAll(".archiveTable__column > a")).map(
      (element) => {
        return { name: element.innerText.trim(), url: element.href };
      }
    );
  });

  await page.close();
  return listOfLeagueSeasons;
};
