import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import ProtectedRoute from './components/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import TaskPage from './pages/TaskPage'
import ShoppingListPage from './pages/ShoppingListPage'
import HomePage from './pages/HomePage'
import TaskForm from './pages/TaskForm'
import ShoppingListForm from './pages/ShoppingListForm'
import ItemPage from './pages/ItemPage'
import ItemForm from './pages/ItemForm'

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={
          <ProtectedRoute>
            <HomePage />
          </ProtectedRoute>
        } />
        <Route path="/tasks" element={
          <ProtectedRoute>
            <TaskPage />
          </ProtectedRoute>
        } />
        <Route path='/tasks/create/' element={
          <ProtectedRoute>
            <TaskForm />
          </ProtectedRoute>
        }/>
        <Route path='/tasks/create/:taskId' element={
          <ProtectedRoute>
            <TaskForm />
          </ProtectedRoute>
        }/>
        <Route path="/shopping" element={
          <ProtectedRoute>
            <ShoppingListPage />
          </ProtectedRoute>
        } />
        <Route path="/shopping/create/" element={
          <ProtectedRoute>
            <ShoppingListForm />
          </ProtectedRoute>
        } />
        <Route path="/shopping/create/:shoppingListIdFromUrl" element={
          <ProtectedRoute>
            <ShoppingListForm />
          </ProtectedRoute>
        } />
        <Route path="/items" element={
          <ProtectedRoute>
            <ItemPage />
          </ProtectedRoute>
        } />      
        <Route path="/items/create" element={
          <ProtectedRoute>
            <ItemForm />
          </ProtectedRoute>
        } />     
        <Route path="/items/create/:itemIdFromUrl" element={
          <ProtectedRoute>
            <ItemForm />
          </ProtectedRoute>
        } />                 
        <Route path="*" element={<Navigate to="/home" replace />} />        
      </Routes>
    </BrowserRouter>
  )
}

export default App