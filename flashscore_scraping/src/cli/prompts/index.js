import chalk from "chalk";

import { BASE_URL, OUTPUT_PATH } from "../../constants/index.js";

import { selectFileType } from "./fileType/index.js";
import { selectCountry } from "./countries/index.js";
import { selectLeague } from "./leagues/index.js";
import { selectSeason } from "./season/index.js";

import { getListOfSeasons } from "../../scraper/services/seasons/index.js";

export const promptUserOptions = async (context, cliOptions) => {
  const fileType = await selectFileType(cliOptions?.fileType);
  const country = await selectCountry(context, cliOptions?.country);
  const season = await resolveSeason(context, cliOptions, country);

  const fileName = generateFileName(country?.name, season?.name);

  console.info(`\n📝 Starting data collection...`);
  console.info(
    `📁 File will be saved to: ${chalk.cyan(
      `${OUTPUT_PATH}/${fileName}${fileType.extension}`
    )}`
  );

  return { fileName, season, fileType };
};

const resolveSeason = async (context, cliOptions, country) => {
  if (!cliOptions?.league) {
    const league = await selectLeague(context, country?.id);
    return await selectSeason(context, league?.url);
  }

  if (!cliOptions?.season) {
    throw Error(
      `❌ Missing required argument: season=<season>\n` +
        `Usage example: country=Poland league=Ekstraklasa season=2026-2027`
    );
  }

  const leagueName = capitalizeWords(cliOptions.league);

  console.info(
    `${chalk.green("✔")} League: ${chalk.cyan(leagueName)}`
  );

  const leagueUrl =
    `${BASE_URL}/football/${country?.name}/${cliOptions.league}`.toLowerCase();

  const seasons = await selectSeasonByName(
    context,
    leagueUrl,
    cliOptions.season
  );

  console.info(
    `${chalk.green("✔")} Season: ${chalk.cyan(seasons.name)}`
  );

  return seasons;
};

const generateFileName = (countryName = "", seasonName = "") => {
  return `${countryName}_${seasonName}`
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, "_")
    .replace(/^_+|_+$/g, "");
};

const capitalizeWords = (str) => {
  return str
    .replace(/[-_]/g, " ")
    .replace(/\b\w/g, (char) => char.toUpperCase());
};

const selectSeasonByName = async (context, leagueUrl, targetSeason) => {
  const seasons = await getListOfSeasons(context, leagueUrl);

  const normalizedTarget = targetSeason
    .toLowerCase()
    .replace(/\s+/g, "")
    .replace(/\//g, "-");

  const selectedSeason = seasons.find((season) => {
    const normalizedSeason = season.name
      .toLowerCase()
      .replace(/\s+/g, "")
      .replace(/\//g, "-");

    return normalizedSeason.includes(normalizedTarget);
  });

  if (!selectedSeason) {
    throw Error(
      `❌ No season found for "${targetSeason}"\n` +
        `Available seasons:\n` +
        seasons.map((season) => `- ${season.name}`).join("\n")
    );
  }

  return selectedSeason;
};
