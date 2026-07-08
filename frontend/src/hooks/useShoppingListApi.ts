import { useEffect, useState } from "react";
import { useAuthStore } from "../store/useAuthStore";

export interface ShoppingList {
    id: string,
    completedAt?: Date,
    name: string,
    notes: string,
    status: string,
    userId: string,
}

export interface useShoppingListApiProps {
    page: number,
    size: number
}

const useShoppingListApi = ({ page, size }: useShoppingListApiProps): {shoppingLists: ShoppingList[], totalPages: number} => {
  const [shoppingLists, setTasks] = useState<ShoppingList[]>([])
  const [totalPages, setTotalPages] = useState<number>(0);
  const token = useAuthStore.getState().token

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/api/shoppinglists?page=${page}&size=${size}`, {
      headers: { "Authorization": `Bearer ${token}`}
    })
      .then(res => res.json())
      .then(data => {setTasks(data.content); setTotalPages(data.page.totalPages); console.log(data)})
  }, [page, size, token])

  return { shoppingLists, totalPages }
}

export default useShoppingListApi;