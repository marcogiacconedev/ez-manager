import { useState } from "react";
import AddButton from "../components/AddButton";
import Header from "../components/Header";
import useShoppingListApi from "../hooks/useShoppingListApi";
import { useNavigate } from "react-router-dom";
import EmptyListRow from "../components/EmptyListRow";

const ShoppingListPage = (): React.ReactNode => {
    const [page, setPage] = useState<number>(0);
    const navigate = useNavigate();
    const resultsPerPage = 4
    const { shoppingLists, totalPages } = useShoppingListApi({ page: page, size: resultsPerPage });
    
    const changePage = (value: number) : void => {
        if (page + value < 0 || page + value >= totalPages) {
            return
        }
        setPage(page + value);
    }

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
                            <p className="shoppinglist-display-item shoppinglist-description">▻ {shoppinglist.notes}</p>
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