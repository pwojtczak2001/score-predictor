import fs from "fs";
import path from "path";

import { OUTPUT_PATH } from "../../constants/index.js";

export const writeJsonToFile = (data, fileName) => {
  // Konwersja na tablicę
  const dataArray = Object.entries(data).map(([matchId, matchData]) => ({
    matchId,
    ...matchData
  }));

  // 2. Sortowanie według daty
  // Zakładamy, że format daty to np. "DD.MM.YYYY HH:mm"
  dataArray.sort((a, b) => parseDate(a.date) - parseDate(b.date));

  const filePath = path.join(OUTPUT_PATH, fileName);
  const fileContent = JSON.stringify(dataArray, null, 2);

  fs.writeFileSync(filePath, fileContent, "utf-8");
};

// Pomocnicza funkcja do parsowania daty
const parseDate = (dateStr) => {
  // Przykładowy format: "31.10. 18:00"
  // Musisz dopasować logikę, jeśli format w Twoich danych jest inny
  const [dayMonth, time] = dateStr.split(" ");
  const [day, month] = dayMonth.split(".");
  const year = new Date().getFullYear(); // Jeśli brak roku w danych
  return new Date(`${year}-${month}-${day}T${time}:00`);
};
