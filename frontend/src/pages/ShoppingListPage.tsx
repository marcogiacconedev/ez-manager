import { useEffect, useState } from "react";
import AddButton from "../components/AddButton";
import Header from "../components/Header";
import  { type ShoppingList } from "../hooks/useShoppingListApi";
import { useNavigate } from "react-router-dom";
import EmptyListRow from "../components/EmptyListRow";
import { useAuthStore } from "../store/useAuthStore";

const ShoppingListPage = (): React.ReactNode => {
    const token = useAuthStore.getState().token;
    const [page, setPage] = useState<number>(0);
    const navigate = useNavigate();
    const resultsPerPage = 4;
    const [shoppingLists, setShoppingLists] = useState<ShoppingList[]>([]);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [openDeleteModal, setOpenDeleteModal] = useState<string>('');    

    const getLists = (): void => {
        fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists?page=${page}&size=${resultsPerPage}`, {
        headers: { "Authorization": `Bearer ${token}`}
        })
        .then(res => res.json())
        .then(data => {setShoppingLists(data.content); setTotalPages(data.page.totalPages); console.log(data)})
    }

    useEffect(() => {
        getLists();
    }, [page])
    
    const changePage = (value: number) : void => {
        if (page + value < 0 || page + value >= totalPages) {
            return
        }
        setPage(page + value);
    }

    const deleteShoppingList = async (shoppingList: ShoppingList): Promise<void> => {
        try {
            const itemsRes = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${shoppingList.id}/items`, {
                method: 'DELETE',
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!itemsRes.ok) {
                throw new Error(`Errore cancellazione items: ${itemsRes.status}`);
            }

            const listRes = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists/${shoppingList.id}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!listRes.ok) {
                throw new Error(`Errore cancellazione lista: ${listRes.status}`);
            }

            getLists();
        } catch (err) {
            console.error("Errore durante l'eliminazione della lista della spesa:", err);
        }
    };

    return (
        <>
            <Header
                header="Shopping"
                username={null}
                isNavigationButtonVisible={true}
            ></Header>
            <div className="card-container">
                <div className="card">
                    <EmptyListRow
                        isRowVisible={shoppingLists.length < 1}
                        text={'No tasks found ♫ ♪'}
                    ></EmptyListRow>                    
                    {
                        shoppingLists.map((shoppinglist) => (
                        <div key={shoppinglist.id} className="shoppinglist-display-row" onClick={() => navigate(`/shopping/create/${shoppinglist.id}`)}>
                            <p className="shoppinglist-display-item shoppinglist-name">▶ {shoppinglist.name}</p>
                            {shoppinglist.notes !== '' && <p className="shoppinglist-display-item shoppinglist-description">▻ {shoppinglist.notes}</p>}                            
                            <div className="delete-item-button-container">
                                {openDeleteModal === shoppinglist.id && (
                                    <button 
                                        className="delete-item-button"
                                        style={{
                                            // border: '1px solid red',
                                            marginRight: '1rem'
                                        }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteShoppingList(shoppinglist);
                                        }}
                                    >Delete?</button>
                                )}                                
                                <button 
                                    className="delete-item-button"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        if (openDeleteModal !== shoppinglist.id) {
                                            setOpenDeleteModal(shoppinglist.id);
                                        } else {
                                            setOpenDeleteModal('');
                                        }
                                    }}
                                >X</button>                                    
                            </div>                        
                            <hr className="shoppinglist-line"/>
                        </div>
                        ))
                    }
                    <div className="paginator-container">
                        <button className="paginator-button" onClick={() => changePage(-1)}>Previous</button>
                        <p className="paginator-button">{page + 1}</p>
                        <button className="paginator-button" onClick={() => changePage(1)}>Next</button>
                    </div>
                    <div className="add-button-container">
                        <AddButton
                            url={'/shopping/create'}
                            text={'Create new'}
                        ></AddButton>     
                    </div>
                </div>
            </div>
        </>
    )
}

export default ShoppingListPage;