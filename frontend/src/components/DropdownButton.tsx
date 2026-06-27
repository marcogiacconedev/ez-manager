export interface DropdownButtonProps {
    header: string,
    onOpen: () => void,
    dropdownOpen: boolean
}

const DropdownButton = ({ header, onOpen, dropdownOpen }: DropdownButtonProps): React.ReactNode => {
    return (
        <>
            <div className="dropdown-button-container" onClick={onOpen}>
                <div className="dropdown-button-header" style={dropdownOpen ? { marginBottom: "1rem" } : {}}>{ header }</div>
                <div className="dropdown-button-arrow">{ dropdownOpen ? '<' : 'v'}</div>
            </div>
        </>
    )
}

export default DropdownButton;