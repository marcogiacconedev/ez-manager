import { useNavigate } from "react-router-dom";

export interface HeaderProps {
    header: string,
    username: string | null,
    isNavigationButtonVisible: boolean
}
const Header = ({ header, username, isNavigationButtonVisible }: HeaderProps): React.ReactNode => {
    const navigate = useNavigate();
    return (
        <div className="header-container">
            <h1 className="header">{header}</h1>
            {username && <h3 className="header-subtitle">{username}</h3>}
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'end'
            }}>
                <h3 className="header-subtitle">Today: {new Date().toDateString()}</h3>
                {isNavigationButtonVisible && <button className="header-home-button" onClick={() => navigate('/home')}>&#60;</button>}
            </div>
        </div>
    )
}

export default Header;