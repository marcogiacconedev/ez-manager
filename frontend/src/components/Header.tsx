export interface HeaderProps {
    header: string,
    username: string | null
}
const Header = ({ header, username }: HeaderProps): React.ReactNode => {
    return (
        <div className="header-container">
            <h1 className="header">{header}</h1>
            {username && <h3 className="header-subtitle">{username}</h3>}
            <h3 className="header-subtitle">Today: {new Date().toDateString()}</h3>
        </div>
    )
}

export default Header;