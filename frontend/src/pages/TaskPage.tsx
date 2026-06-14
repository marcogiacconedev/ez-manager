import { useState } from "react";
import useTaskApi from "../hooks/useTaskApi";
import Calendar from "../components/Calendar";

const TaskPage = (): React.ReactNode => {
    const [page, setPage] = useState<number>(0);
    const resultsPerPage = 3;
    const { tasks, totalPages } = useTaskApi(page, resultsPerPage);

    const changePage = (value: number) : void => {
        if (page + value < 0 || page + value >= totalPages) {
            return
        }
        setPage(page + value);
    }
    return (
        <>
            <div className="header-container">
                <h1 className="header">Task</h1>
                <h3 className="header-subtitle">Oggi: {new Date().toDateString()}</h3>
            </div>
            <div className="card-container">
                <h2 className="header-2">Task</h2>
                <div className="card">
                    {
                        tasks.map((task) => (
                        <div key={task.id} className="task-display-row">
                            <p className="task-display-item task-date">▶ {new Date(task.date).toDateString()}</p>
                            <p className="task-display-item task-name">▻ {task.name}</p>
                            <p className="task-display-item task-description">▻ {task.description}</p>
                            <hr className="task-line"/>
                        </div>
                        ))
                    }
                    <div className="paginator-container">
                        <button className="paginator-button" onClick={() => changePage(-1)}>Previous</button>
                        <p className="paginator-button">{page + 1}</p>
                        <button className="paginator-button" onClick={() => changePage(1)}>Next</button>
                    </div>
                </div>
                <div className="card">
                    <Calendar></Calendar>
                </div>
            </div>
        </>
    )
}

export default TaskPage;