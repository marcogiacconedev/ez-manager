import { useNavigate } from "react-router-dom"

export interface AddButtonProps {
    url: string,
    text: string
}

const AddButton = ({ url, text }: AddButtonProps): React.ReactNode => {
    const navigate = useNavigate();
    return (
        <div className="add-button-container">
            <button className="add-button" onClick={() => navigate(url)}>{ text }+</button>
        </div>
    )
}

export default AddButton;