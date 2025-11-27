import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import { useAuth } from '../contexts/AuthContext'
import './AdminPanel.css'

/**
 * Panel de administración principal.
 * Requiere autenticación y rol de administrador.
 * 
 * @returns {JSX.Element} Panel de administración
 */
function AdminPanel() {
  const { user, token } = useAuth()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (user && token) {
      fetchUsers()
    }
  }, [user, token])

  const fetchUsers = async () => {
    try {
      setLoading(true)
      
      const response = await axios.get('http://localhost:8080/api/users', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      
      setUsers(response.data)
      setError('')
    } catch (err) {
      if (err.response?.status === 401) {
        setError('Sesión expirada. Por favor inicia sesión nuevamente.')
      } else {
        setError('Error al cargar los usuarios')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="admin-panel">
      <div className="container">
        <h1 className="page-title">Panel de Administración</h1>
        <p className="admin-subtitle">Bienvenido, {user.name}</p>

        <div className="admin-menu">
          <Link to="/admin/products" className="admin-menu-card">
            <div className="menu-icon">📦</div>
            <h3>Gestión de Productos</h3>
            <p>Crear, editar y eliminar productos del catálogo</p>
          </Link>
          
          <div className="admin-menu-card">
            <div className="menu-icon">👥</div>
            <h3>Gestión de Usuarios</h3>
            <p>Ver y administrar usuarios del sistema</p>
          </div>
        </div>

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        <div className="admin-section">
          <h2>Gestión de Usuarios</h2>
          
          {loading ? (
            <div className="loading">Cargando usuarios...</div>
          ) : (
            <div className="users-table-container">
              <table className="users-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Fecha de Registro</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((u) => (
                    <tr key={u.id}>
                      <td>{u.id}</td>
                      <td>{u.name}</td>
                      <td>{u.email}</td>
                      <td>
                        <span className={`role-badge role-${u.role.toLowerCase()}`}>
                          {u.role}
                        </span>
                      </td>
                      <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="admin-stats">
          <div className="stat-card">
            <h3>Total Usuarios</h3>
            <p className="stat-number">{users.length}</p>
          </div>
          <div className="stat-card">
            <h3>Administradores</h3>
            <p className="stat-number">
              {users.filter(u => u.role === 'ADMIN').length}
            </p>
          </div>
          <div className="stat-card">
            <h3>Clientes</h3>
            <p className="stat-number">
              {users.filter(u => u.role === 'CUSTOMER').length}
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminPanel

