import { Navigate } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

/**
 * Componente que protege rutas que requieren autenticación.
 * Redirige al login si el usuario no está autenticado.
 * 
 * @param {Object} props - Propiedades del componente
 * @param {React.ReactNode} props.children - Componente a renderizar si está autenticado
 * @param {string} props.requiredRole - Rol requerido (opcional)
 * @param {string} props.redirectTo - Ruta de redirección (por defecto /login)
 * @returns {JSX.Element} Componente protegido o redirección
 */
function ProtectedRoute({ 
  children, 
  requiredRole = null, 
  redirectTo = '/login' 
}) {
  const { isAuthenticated, loading, hasRole } = useAuth()

  // Mostrar loading mientras se verifica la autenticación
  if (loading) {
    return (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '50vh',
        gap: '1rem'
      }}>
        <div className="loading-spinner" style={{
          width: '50px',
          height: '50px',
          border: '4px solid #e2e8f0',
          borderTopColor: '#2563eb',
          borderRadius: '50%',
          animation: 'spin 1s linear infinite'
        }}></div>
        <p style={{ color: '#64748b' }}>Verificando autenticación...</p>
        <style>{`
          @keyframes spin {
            to { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    )
  }

  // Si no está autenticado, redirigir al login
  if (!isAuthenticated) {
    return <Navigate to={redirectTo} replace />
  }

  // Si se requiere un rol específico y el usuario no lo tiene
  if (requiredRole && !hasRole(requiredRole)) {
    return <Navigate to="/" replace />
  }

  // Usuario autenticado (y con el rol correcto si se requiere)
  return children
}

export default ProtectedRoute

