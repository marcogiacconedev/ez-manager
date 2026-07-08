export interface EmptyListRowProps {
    isRowVisible: boolean,
    text: string
}

const EmptyListRow = ({ isRowVisible, text }: EmptyListRowProps): React.ReactNode => {

    return (
        <div className="empty-list-row-container">
            { isRowVisible && (
                <div className="empty-list-row">
                    <p className="empty-list-row-text">{text}</p>
                </div>
            )}
        </div>
    )
}

export default EmptyListRow;