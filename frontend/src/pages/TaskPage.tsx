import { useEffect, useState } from "react";
import Calendar from "../components/Calendar";
import { useNavigate } from "react-router-dom";
import DropdownButton from "../components/DropdownButton";
import AddButton from "../components/AddButton";
import Header from "../components/Header";
import EmptyListRow from "../components/EmptyListRow";
import { useAuthStore } from "../store/useAuthStore";
import type { Task } from "./HomePage";

const TaskPage = (): React.ReactNode => {
    const token = useAuthStore.getState().token;
    const [tasks, setTasks] = useState<Task[]>([]);
    const [page, setPage] = useState<number>(0);
    const [selectedDate, setSelectedDate] = useState<Date | null>(null);
    const resultsPerPage = 4;
    const [totalPages, setTotalPages] = useState<number>(0);
    const navigate = useNavigate();
    const [calendarOpen, setCalendaropen] = useState<boolean>(false);
    const [openDeleteModal, setOpenDeleteModal] = useState<string>('');

    const getTasks = async (): Promise<void> => {
        let url: string = `${import.meta.env.VITE_API_URL}/api/tasks?page=${page}&size=${resultsPerPage}`;
        if (selectedDate) {
            const formattedDate = selectedDate?.toISOString().split("T")[0];
            url = `${url}&date=${formattedDate}`
        }
        fetch(url, {
            headers : {
                "Authorization" : `Bearer ${token}`
            }
        })
        .then(res => res.json())
        .then(data => {
            setTotalPages(data.page.totalPages);
            setTasks(data.content);
        })        
    } 

    useEffect(() => {
        getTasks();
    }, [page, selectedDate])

    const changePage = (value: number) : void => {
        if (page + value < 0 || page + value >= totalPages) {
            return
        }
        setPage(page + value);
    }

    const onSelectDate = (date: Date | null): void => {
        setSelectedDate(date);
    }

    const toggleCalendarDropdown = (): void => {
        setCalendaropen(!calendarOpen);
    }

    const deleteTask = (task: Task): void => {
        console.log(task);
        fetch(`${import.meta.env.VITE_API_URL}/api/tasks/${task.id}`, {
            method: 'DELETE',
            headers: {
                "Authorization" : `Bearer ${token}`
            }
        })
        .then(() => getTasks());
    }

    return (
        <>
            <Header
                header="Task"
                username={null}
                isNavigationButtonVisible={true}
            ></Header>
            <div className="card-container">
                <div className="card">
                    <EmptyListRow
                        isRowVisible={tasks.length < 1}
                        text={'No tasks found ♫ ♪'}
                    ></EmptyListRow>                    
                    {
                        tasks.map((task) => (
                        <div key={task.id} className="task-display-row" onClick={() => navigate(`/tasks/create/${task.id}`)}>
                            <div className={`task-completed-led ${task.completedAt ? 'completed' : ''}`}></div>
                            <p className="task-display-item task-date">▶ {new Date(task.date).toDateString()}</p>
                            <p className="task-display-item task-name">▻ {task.name}</p>
                            {task.description !== '' && <p className="task-display-item task-description">▻ {task.description}</p>}
                            <div className="delete-item-button-container">
                                {openDeleteModal === task.id && (
                                    <button 
                                        className="delete-item-button"
                                        style={{
                                            // border: '1px solid red',
                                            marginRight: '1rem'
                                        }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteTask(task);
                                        }}
                                    >Delete?</button>
                                )}                                
                                <button 
                                    className="delete-item-button"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        if (openDeleteModal !== task.id) {
                                            setOpenDeleteModal(task.id);
                                        } else {
                                            setOpenDeleteModal('');
                                        }
                                    }}
                                >X</button>
                            </div>
                            <hr className="task-line"/>
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
                            url={'/tasks/create'}
                            text={'Create new'}
                        ></AddButton>     
                    </div>
                </div>           
                <div className="card">
                    <DropdownButton
                        header={'Date'}
                        onOpen={toggleCalendarDropdown}
                        dropdownOpen={calendarOpen}
                        marginTop="0"
                        marginBottom="0"                    
                    ></DropdownButton>
                    {calendarOpen && (                    
                        <Calendar
                            selectedDate={selectedDate ? selectedDate : new Date()}
                            onSelectDate={date => onSelectDate(date)}
                            onReset={() => onSelectDate(null)}
                            isResetButtonVisible={true}
                        ></Calendar>                        
                    )}
                </div>
            </div>
        </>
    )
}

export default TaskPage;