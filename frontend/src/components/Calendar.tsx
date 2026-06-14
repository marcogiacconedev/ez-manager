import { useState } from "react";

// ─── Tipi ────────────────────────────────────────────────────────────────────

type CalendarProps = {
  onSelectDate?: (date: Date) => void;
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

// ─── Costanti ────────────────────────────────────────────────────────────────

const MONTHS: string[] = [
  "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
  "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre",
];

const DAYS: string[] = ["Lu","Ma","Me","Gi","Ve","Sa","Do"];

// ─── Helper ──────────────────────────────────────────────────────────────────

function daysInMonth(year: number, month: number): number {
  return new Date(year, month + 1, 0).getDate();
}

// Restituisce 0=Lunedì … 6=Domenica
function firstDayOfMonth(year: number, month: number): number {
  const d = new Date(year, month, 1).getDay();
  return d === 0 ? 6 : d - 1;
}

// ─── Componente ──────────────────────────────────────────────────────────────

export default function Calendar({ onSelectDate }: CalendarProps) {
  const today = new Date();

  const [cur, setCur] = useState<MonthState>({
    year: today.getFullYear(),
    month: today.getMonth(),
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
    const date = new Date(cur.year, cur.month, day);
    setSelected({ day, month: cur.month, year: cur.year });
    onSelectDate?.(date);
  }

  function isToday(day: number): boolean {
    return (
      day === today.getDate() &&
      cur.month === today.getMonth() &&
      cur.year === today.getFullYear()
    );
  }

  function isSelected(day: number): boolean {
    return (
      selected !== null &&
      day === selected.day &&
      cur.month === selected.month &&
      cur.year === selected.year
    );
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

/* ---------- CSS (Calendar.module.css o file globale) ----------

.calendar {
  width: 320px;
  font-family: sans-serif;
}

.calendar__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.calendar__header button {
  background: none;
  border: 1px solid #ddd;
  border-radius: 6px;
  width: 32px;
  height: 32px;
  cursor: pointer;
  font-size: 18px;
  line-height: 1;
}

.calendar__grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px;
}

.calendar__dow {
  text-align: center;
  font-size: 11px;
  color: #888;
  padding-bottom: 6px;
}

.calendar__day {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  border-radius: 50%;
  cursor: pointer;
}

.calendar__day:hover     { background: #f0f0f0; }
.calendar__day--other    { color: #bbb; cursor: default; }
.calendar__day--today    { background: #e8f0fe; color: #1a73e8; font-weight: 600; }
.calendar__day--selected { background: #1a73e8; color: #fff; font-weight: 600; }

*/
