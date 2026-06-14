import { useEffect, useState } from "react";
import { useAuthStore } from "../store/useAuthStore";
import type { Task } from "../pages/HomePage";

function useTaskApi(page: number, size: number) : {tasks: Task[], totalPages: number} {
  const [tasks, setTasks] = useState<Task[]>([])
  const [totalPages, setTotalPages] = useState<number>(0);
  const token = useAuthStore.getState().token

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/api/tasks?page=${page}&size=${size}`, {
      headers: { "Authorization": `Bearer ${token}`}
    })
      .then(res => res.json())
      .then(data => {setTasks(data.content); setTotalPages(data.page.totalPages); console.log(data)})
  }, [page, size, token])

  return { tasks, totalPages }
}

export default useTaskApi;