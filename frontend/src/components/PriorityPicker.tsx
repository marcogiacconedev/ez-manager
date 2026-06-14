interface PriorityPickerProps {
    onSelect: (priority: number) => void
    priority: number
}

const PriorityPicker = ({ onSelect, priority }: PriorityPickerProps): React.ReactNode => {
  return (
    <div className="prio-picker-container">
      <p className="prio-text">Prio: </p>
      <button className={`prio-picker-button ${priority === 1 ? 'prio-picker-button-selected' : ''}`} onClick={() => onSelect(1)}>1</button>
      <button className={`prio-picker-button ${priority === 2 ? 'prio-picker-button-selected' : ''}`} onClick={() => onSelect(2)}>2</button>
      <button className={`prio-picker-button ${priority === 3 ? 'prio-picker-button-selected' : ''}`} onClick={() => onSelect(3)}>3</button>
    </div>
  )
}

export default PriorityPicker;