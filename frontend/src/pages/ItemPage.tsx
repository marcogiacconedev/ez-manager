import { useEffect, useState } from "react";
import EmptyListRow from "../components/EmptyListRow";
import Header from "../components/Header"
import { useAuthStore } from "../store/useAuthStore";
import { useNavigate } from "react-router-dom";
import AddButton from "../components/AddButton";

export interface ShoppingItem {
    category: string,
    id: string,
    measure: string,
    name: string,
    price: number,
    size: number,
    userId: string
}

const ItemPage = (): React.ReactNode => {
    const token = useAuthStore.getState().token;
    const [items, setItems] = useState<ShoppingItem[]>([]);
    const [page, setPage] = useState<number>(0);
    const [totalPages, setTotalPages] = useState<number>(0);
    const size: number = 6;
    const navigate = useNavigate();

    const getItems = (): void => {
        fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems?page=${page}&size=${size}`, {
            headers: {
                "Authorization" : `Bearer ${token}`
            }
        }).then(res => res.json()).then(res => {
            setItems(res.content); 
            setTotalPages(res.page.totalPages);
            console.log(res);
        });
    }

    useEffect(() => {
        getItems();
    }, [page])

    const changePage = (value: number) : void => {
        if (page + value < 0 || page + value >= totalPages) {
            return
        }
        setPage(page + value);
    }

    return (
        <>
            <Header
               header="Items"
               isNavigationButtonVisible={true}
               username={null} 
            ></Header>
            <div className="card-container">
                <div className="card">
                    <EmptyListRow
                        isRowVisible={items.length < 1}
                        text={'No items found ♫ ♪'}
                    ></EmptyListRow>        
                        {items.map((item) => (
                            <div key={item.id} className="task-display-row" onClick={() => navigate(`/items/create/${item.id}`)}>                                                            
                                <p className="task-display-item task-date">▻ {item.name}</p>
                                <p className="task-display-item task-description">▻ {item.category}</p>
                                <p className="task-display-item task-description">▻ {item.size} {item.measure} - {item.price} €</p>
                                <hr className="task-line"/>
                            </div>
                        ))}  
                    <div className="paginator-container">
                        <button className="paginator-button" onClick={() => changePage(-1)}>Previous</button>
                        <p className="paginator-button">{page + 1}</p>
                        <button className="paginator-button" onClick={() => changePage(1)}>Next</button>
                    </div>   
                    <AddButton
                        url={'/items/create'}
                        text={'Create new'}
                    ></AddButton>                                
                </div>
            </div>
        </>
    )
}

export default ItemPage;