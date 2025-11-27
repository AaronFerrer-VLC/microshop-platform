import { useAuth } from '../contexts/AuthContext'
import './UserProfile.css'

/**
 * Página de perfil de usuario.
 * Muestra la información del usuario autenticado.
 * 
 * @returns {JSX.Element} Página de perfil
 */
function UserProfile() {
  const { user } = useAuth()

  if (!user) {
    return null
  }

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    return new Date(dateString).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const getRoleLabel = (role) => {
    const roles = {
      'ADMIN': 'Administrador',
      'CUSTOMER': 'Cliente',
      'SELLER': 'Vendedor'
    }
    return roles[role] || role
  }

  const getRoleBadgeClass = (role) => {
    const classes = {
      'ADMIN': 'role-badge-admin',
      'CUSTOMER': 'role-badge-customer',
      'SELLER': 'role-badge-seller'
    }
    return classes[role] || ''
  }

  return (
    <div className="user-profile-page">
      <div className="container">
        <h1 className="page-title">Mi Perfil</h1>

        <div className="profile-card">
          <div className="profile-header">
            <div className="profile-avatar">
              <span className="avatar-icon">👤</span>
            </div>
            <div className="profile-info-header">
              <h2>{user.name}</h2>
              <span className={`role-badge ${getRoleBadgeClass(user.role)}`}>
                {getRoleLabel(user.role)}
              </span>
            </div>
          </div>

          <div className="profile-details">
            <div className="detail-section">
              <h3>Información Personal</h3>
              <div className="detail-grid">
                <div className="detail-item">
                  <label>Nombre Completo</label>
                  <p>{user.name}</p>
                </div>
                <div className="detail-item">
                  <label>Email</label>
                  <p>{user.email}</p>
                </div>
                <div className="detail-item">
                  <label>Rol</label>
                  <p>
                    <span className={`role-badge ${getRoleBadgeClass(user.role)}`}>
                      {getRoleLabel(user.role)}
                    </span>
                  </p>
                </div>
                <div className="detail-item">
                  <label>Fecha de Registro</label>
                  <p>{formatDate(user.createdAt)}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserProfile

