import { useEffect, useState } from "react";
import Header from "../components/Header";
import { useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import DropdownButton from "../components/DropdownButton";
import EmptyListRow from "../components/EmptyListRow";

export interface ShoppingListRequest {
    name: string,
    completedAt: Date | null,
    createdAt: Date,
    status: string,
    notes: string
}

export interface ShoppingListItem {
    added: boolean,
    id?: string,
    itemName: string,
    listName: string,
    price?: number,
    quantity: number,
    shoppingItemId: string,
    shoppingListId?: string,
    category: string,
    measure: string
}

export interface UpdateShoppingListItemRequest {
    added: boolean,
    category: string,
    id: string | undefined,
    itemName: string,
    listName: string,
    price?: number,
    quantity: number,
    shoppingListId?: string,
    shoppingItemId: string,
    notes: string
}

export interface Item {
    category: string,
    id: string,
    measure: string,
    name: string,
    price: number,
    size: number,
    userId: string
}

export interface CreateListItemRequest {
    quantity: number,
    itemId: string
}

export interface UpdateListItemRequest {
    added: boolean,
    quantity: number
}

const ShoppingListForm = (): React.ReactNode => {
    let { shoppingListIdFromUrl } = useParams<{ shoppingListIdFromUrl: string }>();
    const [shoppingListId, setShoppingListId] = useState<string>('');
    const token = useAuthStore.getState().token;
    const [name, setName] = useState<string>('');
    const [status, setStatus] = useState<string>('PENDING');
    const [notes, setNotes] = useState<string>('');
    const [shoppingListItems, setShoppingListItems] = useState<ShoppingListItem[]>([]);
    const [error, setError] = useState<string>('');
    const [isListOpen, setIsListOpen] = useState<boolean>(true);
    const [isNotesDropdownOpen, setIsNotesDropdownOpen] = useState<boolean>(false);
    const [isAddItemsOpen, setIsAddItemsOpen] = useState<boolean>(false);
    const [availableItems, setAvailableItems] = useState<Item[]>([]);
    const [filteredAvailableItems, setFilteredAvailableItems] = useState<Item[]>([]);

    useEffect(() => {
        
        // se siamo in modalita edit prende i metadati della lista
        if (shoppingListIdFromUrl) {
            fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists/${shoppingListIdFromUrl}`, {
                headers: { "Authorization": `Bearer ${token}`}
            })
            .then(res => res.json())
            .then(res => {
                setName(res.name);
                setStatus(res.status);
                setNotes(res.notes);
                setShoppingListId(res.id);
            })
            // prende gli item aggiunti in lista
            fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${shoppingListIdFromUrl}/items`, {
                headers: {"Authorization" : `Bearer ${token}`}
            })
            .then(res => res.json())
            .then(res => {setShoppingListItems(res.content); console.log(res)})
        }
        // prende gli item aggiungibili alla lista
        fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems`, {
            headers: { "Authorization" : `Bearer ${token}`}
        })
        .then(res => res.json())
        .then(res => {
            setAvailableItems(res);
            setFilteredAvailableItems(res);
        })
    }, [])

    const updateItem = (index: number, field: keyof typeof shoppingListItems[number], value: string | number | boolean) => {
        setShoppingListItems(prev =>
            prev.map((item, i) =>
                i === index ? { ...item, [field]: value } : item
            )
        );
    };

    const addItemToList = (item: Item): void => {
        const itemToAdd: ShoppingListItem = {
            added: false,
            id: undefined,
            itemName: item.name,
            listName: name,
            price: item.price,
            quantity: 1,
            measure: item.measure,
            shoppingItemId: item.id,
            shoppingListId: shoppingListIdFromUrl,
            category: item.category
        };
        setShoppingListItems(prev => [...prev, itemToAdd]);
    }

    const removeItemFromList = (item: ShoppingListItem): void => {
        setShoppingListItems(prev =>
            prev.filter(i => i.shoppingItemId !== item.shoppingItemId)
        );
    };

    const updateItemFilter = (filter: string): void => {
        const filtered: Item[] = [];

        if (!filter) {
            setFilteredAvailableItems(availableItems);
            return;
        };

        availableItems.forEach(availableItem => {
            if (
                availableItem.name.toLocaleLowerCase().includes(filter.toLowerCase()) ||
                availableItem.category.toLocaleLowerCase().includes(filter.toLowerCase())
            ) filtered.push(availableItem);
        })

        setFilteredAvailableItems(filtered);
    }

    const isAvailableItemAdded = (availableItem: Item): boolean => {
        let added: boolean = false;
        shoppingListItems.forEach(item => {
            if (item.shoppingItemId === availableItem.id) added = true;
        })

        return added;
    }

    const createNewList = async (): Promise<string> => {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                name: name,
                completedAt: null,
                createdAt: new Date(),
                status: 'PENDING',
                notes: notes
            })
        });

        if (!response.ok) {
            throw new Error("Errore nella creazione della lista");
        }

        const data = await response.json();
        return data.id;    
    }

    const submitForm = async (): Promise<void> => {
        try {
            let newShoppingListId: string = '';
            if (!shoppingListId) {
                newShoppingListId = await createNewList();
            } else {
                newShoppingListId = shoppingListId;
            }
            // ora costruisci il body USANDO currentListId, non shoppingListId dello state
            const requestBody: UpdateShoppingListItemRequest[] = shoppingListItems.map(item => ({
                added: item.added,
                category: item.category,
                id: item.id,
                itemName: item.itemName,
                listName: name,
                price: item.price,
                quantity: item.quantity,
                shoppingItemId: item.shoppingItemId,
                shoppingListId: newShoppingListId,  
                notes: notes
            }));

            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${newShoppingListId}/items`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error("Errore nella chiamata api");
            }

            const data = await response.json();
            console.log(data);
        } catch (error) {
            console.log(error);
        } finally {
            // caricamento
        }
    };

    return (
        <>
            <Header
                header="Shopping"
                username={null}
                isNavigationButtonVisible={true}
            ></Header>
            <div className="card">
                <input type="text" placeholder="name" className="form-input" value={name} onChange={e => {setName(e.target.value)}}/>
                <DropdownButton
                    header="items"
                    dropdownOpen={isListOpen}
                    onOpen={() => setIsListOpen(!isListOpen)}
                    marginTop="1.5rem"
                    marginBottom="0"
                ></DropdownButton>
                {isListOpen && (
                    <>
                            <EmptyListRow
                                isRowVisible={shoppingListItems.length < 1}
                                text="Add some items ♫ ♪"
                            ></EmptyListRow>                    
                        <table>
                            { shoppingListItems.length > 0 && (
                                <>
                                    <thead>
                                        <tr className="table-row">
                                            <td className="shopping-item-name table-header-item">Name</td>
                                            <td className="shopping-item-price table-header-item">P</td>
                                            <td className="shopping-item-quantity table-header-item">Q</td>
                                            <td className="shopping-item-added table-header-item">A</td>
                                            <td></td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        { shoppingListItems.map((item, index) => (
                                            <tr className="table-row list-item" key={item.shoppingItemId}>
                                                <td className="table-item">
                                                    <input
                                                        type="text"
                                                        className="shopping-item-name-input"
                                                        value={item.itemName}
                                                        onChange={e => updateItem(index, 'itemName', e.target.value)}
                                                    />
                                                </td>
                                                <td className="table-item">
                                                    <input
                                                        className="shopping-item-price-input"
                                                        type="text"
                                                        value={item.price}
                                                        onChange={e => updateItem(index, 'price', e.target.value)}
                                                    />
                                                </td>
                                                <td className="table-item">
                                                    <input
                                                        className="shopping-item-quantity-input"
                                                        type="text"
                                                        value={item.quantity}
                                                        onChange={e => updateItem(index, 'quantity', e.target.value)}
                                                    />
                                                </td>
                                                <td className="table-item">
                                                    <input
                                                        className="shopping-item-added-input"
                                                        type="checkbox"
                                                        checked={item.added}
                                                        onChange={e => updateItem(index, 'added', e.target.checked)}
                                                    />
                                                </td>
                                                <td className="table-item">
                                                    <button className="add-item-button" onClick={() => removeItemFromList(item)}>-</button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>                                                        
                                </>
                            )}
                        </table>                    
                    </>
                )}

                <DropdownButton
                    header="add items"
                    dropdownOpen={isAddItemsOpen}
                    onOpen={() => {setIsAddItemsOpen(!isAddItemsOpen)}}
                    marginTop="1.5rem"
                    marginBottom="0"                    
                ></DropdownButton>
                {isAddItemsOpen && (
                    <>
                        <input type="text" className="search-select-input" placeholder="Search items"
                            onChange={e => updateItemFilter(e.target.value)}
                        />
                        <table>
                            <thead>
                                <tr className="table-row">
                                    <td className="shopping-item-name table-header-item">Name</td>
                                    <td className="shopping-item-price table-header-item">Price</td>
                                    <td className="shopping-item-category table-header-item">Category</td>
                                    <td></td>
                                </tr>
                            </thead>
                            <tbody>
                                { filteredAvailableItems.map(item => (
                                    <tr key={item.id} className="table-row list-item">
                                        <td>{item.name}</td>
                                        <td>{item.price}</td>
                                        <td>{item.category}</td>
                                        <td>
                                            {!isAvailableItemAdded(item) && <button className="add-item-button" onClick={() => addItemToList(item)}>+</button>}
                                        </td>
                                    </tr>   
                                ))}                                 
                            </tbody>                                 
                        </table>              
                    </>
                )}                
                <DropdownButton
                    header="notes"
                    dropdownOpen={isNotesDropdownOpen}
                    onOpen={() => setIsNotesDropdownOpen(!isNotesDropdownOpen)}
                    marginTop="1.5rem"
                    marginBottom="0"                    
                ></DropdownButton>
                {isNotesDropdownOpen &&
                    <textarea name="notes" id="notes" placeholder="notes" className="form-textarea" value={notes} onChange={e => {setNotes(e.target.value)}}></textarea>            
                }
                <div className="submit-form-container">
                    <button className="submit-form-button" onClick={submitForm}>{shoppingListId ? 'Apply Changes' : 'Submit'}</button>
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

export default ShoppingListForm; 