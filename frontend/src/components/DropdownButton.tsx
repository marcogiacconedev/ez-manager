export interface DropdownButtonProps {
    header: string,
    onOpen: () => void,
    dropdownOpen: boolean,
    marginTop: string,
    marginBottom: string
}

const DropdownButton = ({ header, onOpen, dropdownOpen, marginTop, marginBottom }: DropdownButtonProps): React.ReactNode => {
    return (
        <>
            <div className="dropdown-button-container" 
                style={{
                    marginTop: marginTop,
                    marginBottom
                }} 
                onClick={onOpen}>
                <div className="dropdown-button-header" style={dropdownOpen ? { marginBottom: "1rem" } : {}}>{ header }</div>
                <div className="dropdown-button-arrow">{ dropdownOpen ? '<' : 'v'}</div>
            </div>
        </>
    )
}

export default DropdownButton;