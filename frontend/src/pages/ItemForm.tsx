import { useEffect, useState } from "react";
import Header from "../components/Header";
import { useNavigate, useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";

const ItemForm = () : React.ReactNode => {
    const token = useAuthStore.getState().token;
    const { itemIdFromUrl } = useParams<{ itemIdFromUrl: string }>();
    const [itemId, setItemId] = useState<string>('');
    const [category, setCategory] = useState<string>('');
    const [name, setName] = useState<string>('');
    const [price, setPrice] = useState<string>('');
    const [size, setSize] = useState<string>('');
    const [measure, setMeasure] = useState<string>('');
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    const getFormData = (): void => {
        fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems/${itemIdFromUrl}`, {
            headers: {
                "Authorization" : `Bearer ${token}`
            }
        }).then(res => res.json())        
        .then(res => {
            setItemId(res.id ?? '');
            setCategory(res.category ?? '');
            setMeasure(res.measure ?? '');
            setName(res.name ?? '');
            setPrice(res.price ?? '');
            setSize(res.size ?? '');
        })
    }

    useEffect(() => {
        if (itemIdFromUrl) {
            getFormData();            
        }
    }, [])

    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        const val = e.target.value;
        // accetta solo numeri con al massimo 2 decimali
        if (val === '' || /^\d*\.?\d{0,2}$/.test(val)) {
            setPrice(val);
        }
    };

    const handleSizeChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        const val = e.target.value;
        // accetta solo numeri con al massimo 2 decimali 
        if (val === '' || /^\d*\.?\d{0,2}$/.test(val)) {
            setSize(val);
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        const allowed = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', '.'];
        if (allowed.includes(e.key)) return;
        if (!/^\d$/.test(e.key)) e.preventDefault();
    };

    const createNewItem = async (): Promise<void> => {
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems`, {
                method: 'POST',
                headers: {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${token}`
                },
                body: JSON.stringify({
                    category: category,
                    name: name,
                    price: price,
                    size: size,
                    measure: measure
                })
            })        
            const data = await res.json();
            console.log(data);
        } catch (error) {
            setError('An error occourred!');
            console.log(error);
        } finally {
            // loading
        }
    }

    const updateItem = async (): Promise<void> => {
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems/${itemId}`, {
                method: 'PUT',
                headers: {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${token}`
                },
                body: JSON.stringify({
                    category : category,
                    name : name,
                    price : price,
                    size : size,
                    measure : measure
                })
            })

            const data = await res.json();
            console.log(data);
        } catch (error) {
            setError('An error occourred!');
            console.log(error);
        } finally {
            // loading
        }
    }
    
    const submitForm = async (): Promise<void> => {
        if (itemId) { await updateItem() } else { await createNewItem() }
        navigate('/items');
    }

    return (
        <>
            <Header
                header="Items"
                isNavigationButtonVisible={true}
                username={null}
            ></Header>
            <div className="card">
                <input type="text" placeholder="name" className="form-input" value={name} onChange={e => {setName(e.target.value)}}/>
                <input type="text" placeholder="category" className="form-input" value={category} onChange={e => {setCategory(e.target.value)}}/>
                <input
                    type="text"
                    inputMode="decimal"
                    placeholder="price"
                    className="form-input"
                    value={price}
                    onChange={handlePriceChange}
                    onKeyDown={handleKeyDown}
                />
                <input 
                    type="text" 
                    placeholder="size" 
                    className="form-input" 
                    value={size} 
                    onChange={handleSizeChange}
                    onKeyDown={handleKeyDown}
                />
                <input type="text" placeholder="measure" className="form-input" value={measure} onChange={e => {setMeasure(e.target.value)}}/>
                <div className="submit-form-container">
                    <button className="submit-form-button" onClick={submitForm}>{itemId ? 'Apply Changes' : 'Submit'}</button>
                    { error && 
                        <div className="error-message-container">
                            <p className="error-message">{error}</p>
                        </div>
                    }
                </div>                
            </div>
        </>
    )
}

export default ItemForm;