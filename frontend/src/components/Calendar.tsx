import { useState } from "react";

// types

type CalendarProps = {
  onSelectDate?: (date: Date) => void;
  selectedDate: Date | null
};

type MonthState = {
  year: number;
  month: number; // 0–11
};

type SelectedDay = {
  day: number;   // 1–31
  month: number; // 0–11
  year: number;
} | null;

// constants

const MONTHS: string[] = [
  "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
  "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre",
];

const DAYS: string[] = ["Lu","Ma","Me","Gi","Ve","Sa","Do"];

// Helper

function daysInMonth(year: number, month: number): number {
  return new Date(year, month + 1, 0).getDate();
}

// Restituisce 0=Lunedì … 6=Domenica
function firstDayOfMonth(year: number, month: number): number {
  const d = new Date(year, month, 1).getDay();
  return d === 0 ? 6 : d - 1;
}

export default function Calendar({ onSelectDate, selectedDate }: CalendarProps) {
  let date: Date;
  if (selectedDate) {
    date = new Date(selectedDate);
  } else {
    date = new Date();
  }
  const [cur, setCur] = useState<MonthState>({
    year: date.getFullYear(),
    month: date.getMonth(),
  });

  const [selected, setSelected] = useState<SelectedDay>(null);

  const total     = daysInMonth(cur.year, cur.month);
  const start     = firstDayOfMonth(cur.year, cur.month);
  const prevTotal = daysInMonth(cur.year, cur.month - 1);

  const prevDays: number[] = Array.from({ length: start }, (_, i) => prevTotal - start + i + 1);
  const curDays:  number[] = Array.from({ length: total }, (_, i) => i + 1);
  const remaining = (start + total) % 7 === 0 ? 0 : 7 - ((start + total) % 7);
  const nextDays: number[] = Array.from({ length: remaining }, (_, i) => i + 1);

  function prevMonth(): void {
    setCur(c => c.month === 0
      ? { year: c.year - 1, month: 11 }
      : { year: c.year, month: c.month - 1 });
  }

  function nextMonth(): void {
    setCur(c => c.month === 11
      ? { year: c.year + 1, month: 0 }
      : { year: c.year, month: c.month + 1 });
  }

  function selectDay(day: number): void {
    const date = new Date(cur.year, cur.month, day, 12, 0, 0);
  console.log(date);
  console.log(date.toISOString());
    setSelected({ day, month: cur.month, year: cur.year });
    onSelectDate?.(date);
  }

  function isToday(day: number): boolean {
    if (selectedDate) {
      return (
        day === date.getDate() &&
        cur.month === date.getMonth() &&
        cur.year === date.getFullYear()
      );
    } else {
      return false;
    }

  }

  function isSelected(day: number): boolean {
    if (selectedDate) {
      return (
        selected !== null &&
        day === date.getDate() &&
        cur.month === date.getMonth() &&
        cur.year === date.getFullYear()
      );
    } else {
      return false;
    }

  }

  return (
    <div className="calendar">
      <div className="calendar__header">
        <button onClick={prevMonth} aria-label="Mese precedente">‹</button>
        <span>{MONTHS[cur.month]} {cur.year}</span>
        <button onClick={nextMonth} aria-label="Mese successivo">›</button>
      </div>

      <div className="calendar__grid">
        {DAYS.map(d => (
          <div key={d} className="calendar__dow">{d}</div>
        ))}

        {prevDays.map(d => (
          <div key={`prev-${d}`} className="calendar__day calendar__day--other">{d}</div>
        ))}

        {curDays.map(d => (
          <div
            key={d}
            onClick={() => selectDay(d)}
            className={[
              "calendar__day",
              isSelected(d) ? "calendar__day--selected" : "",
              isToday(d)    ? "calendar__day--today"    : "",
            ].join(" ").trim()}
          >
            {d}
          </div>
        ))}

        {nextDays.map(d => (
          <div key={`next-${d}`} className="calendar__day calendar__day--other">{d}</div>
        ))}
      </div>

      {selected !== null && (
        <p className="calendar__selected">
          Selezionato: {selected.day} {MONTHS[selected.month]} {selected.year}
        </p>
      )}
    </div>
  );
}