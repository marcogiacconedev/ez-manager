import { useEffect, useState } from "react";
import Header from "../components/Header";
import { useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import DropdownButton from "../components/DropdownButton";

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
    name: string,
    price?: number,
    quantity: number,
    shoppingListId?: string
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
    let { shoppingListId } = useParams<{ shoppingListId: string }>();
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
            shoppingListId: shoppingListId,
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

    const saveShoppingList = async (): Promise<string> => {
        const requestBody: ShoppingListRequest = {
            name: name,
            completedAt: null,
            createdAt: new Date(),
            status: status,
            notes: notes
        }
        let listId: string;
        if (!shoppingListId) {
            // caso create
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists`, {
                method: 'POST',
                headers: {
                    "Authorization" : `Bearer ${token}`,
                    "Content-Type" : "application/json"
                },
                body: JSON.stringify(requestBody)
            })

            const data = await response.json();
            listId = data.id;
        } else {
            // caso edit
            fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists/${shoppingListId}`, {
                method: 'PUT',
                headers: {
                    "Authorization" : `Bearer ${token}`,
                    "Content-Type" : "application/json"
                }, 
                body: JSON.stringify(requestBody)
            })
            listId = shoppingListId;
        }

        return listId;
    }

    const saveShoppingItems = async (newListId: string): Promise<void> => {
        console.log(newListId);
        shoppingListItems.forEach(item => {
            if (!item.id) {
                const body: CreateListItemRequest = {
                    quantity: item.quantity,
                    itemId: item.shoppingItemId
                }
                // caso aggiunta nuova
                fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${newListId}/items`, {
                    method: 'POST',
                    headers: {
                        "Content-Type" : "application/json",
                        "Authorization" : `Bearer ${token}`
                    },
                    body: JSON.stringify(body)
                })
            } else {
                // caso edit
                const body: UpdateListItemRequest = {
                    added: item.added,
                    quantity: item.quantity
                }
                fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${shoppingListId}/items/${item.shoppingItemId}`, {
                    method: 'PUT',
                    headers: {
                        "Content-Type" : "application/json",
                        "Authorization" : `Bearer ${token}`
                    },
                    body: JSON.stringify(body)
                }).then(() => {
                    const body: UpdateShoppingListItemRequest = {
                        added: item.added,
                        category: item.category,
                        name: item.itemName,
                        price: item.price,
                        quantity: item.quantity,
                        shoppingListId: shoppingListId
                    }

                    // put di tutti gli ITEM, potrebbero essere stati modificati (quando si cambiano nome e quantità)
                    fetch(`${import.meta.env.VITE_API_URL}/api/shoppingitems/${item.shoppingItemId}`, {
                        method: 'PUT',
                        headers: {
                            "Authorization" : `Bearer ${token}`,
                            "Content-Type" : "application/json"
                        }, 
                        body: JSON.stringify(body)
                    })
                })
            }
        })   
    }

    const submitForm = async (): Promise<void> => {
        // chiamata metadati
        try {
            const listId: string = await saveShoppingList();
            console.log(listId);
            await saveShoppingItems(listId);
        } catch (error) {
            console.log(error);
        } finally {
            //caricamento
        } 
    }

    useEffect(() => {
        // se siamo in modalita edit prende i metadati della lista
        if (shoppingListId) {
            fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists/${shoppingListId}`, {
                headers: { "Authorization": `Bearer ${token}`}

            })
            .then(res => res.json())
            .then(res => {
                setName(res.name);
                setStatus(res.status);
                setNotes(res.notes);
            })

            // prende gli item aggiunti in lista
            fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglistitems/${shoppingListId}/items`, {
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
                    onOpen={() => {setIsListOpen(!isListOpen)}}
                ></DropdownButton>
                {isListOpen && 
                    <table>
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
                            {shoppingListItems.map((item, index) => (
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
                    </table>
                }

                <hr className="shoppinglist-line"/>
                <DropdownButton
                    header="add items"
                    dropdownOpen={isAddItemsOpen}
                    onOpen={() => {setIsAddItemsOpen(!isAddItemsOpen)}}
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
                <hr className="shoppinglist-line"/>
                
                <DropdownButton
                    header="notes"
                    dropdownOpen={isNotesDropdownOpen}
                    onOpen={() => setIsNotesDropdownOpen(!isNotesDropdownOpen)}
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