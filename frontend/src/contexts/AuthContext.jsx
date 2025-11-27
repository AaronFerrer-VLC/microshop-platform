import { createContext, useContext, useState, useEffect } from 'react'
import authService from '../services/api/authService'

/**
 * Contexto de autenticación.
 * Proporciona estado y funciones relacionadas con la autenticación del usuario.
 * 
 * @type {React.Context}
 */
const AuthContext = createContext(null)

/**
 * Hook personalizado para acceder al contexto de autenticación.
 * 
 * @returns {Object} Contexto de autenticación
 * @throws {Error} Si se usa fuera del AuthProvider
 */
export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider')
  }
  return context
}

/**
 * Proveedor del contexto de autenticación.
 * Gestiona el estado de autenticación y proporciona funciones de login/logout.
 * 
 * @param {Object} props - Propiedades del componente
 * @param {React.ReactNode} props.children - Componentes hijos
 * @returns {JSX.Element} Proveedor de autenticación
 */
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(null)
  const [loading, setLoading] = useState(true)

  /**
   * Carga el estado de autenticación desde localStorage al iniciar.
   */
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        const storedToken = localStorage.getItem('token')
        const storedUser = localStorage.getItem('user')

        if (storedToken && storedUser) {
          // Validar que el token aún sea válido
          const isValid = await authService.validateToken(storedToken)
          
          if (isValid) {
            setToken(storedToken)
            setUser(JSON.parse(storedUser))
          } else {
            // Token inválido, limpiar localStorage
            localStorage.removeItem('token')
            localStorage.removeItem('user')
          }
        }
      } catch (error) {
        console.error('Error al inicializar autenticación:', error)
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      } finally {
        setLoading(false)
      }
    }

    initializeAuth()
  }, [])

  /**
   * Realiza el login del usuario.
   * 
   * @param {string} email - Email del usuario
   * @param {string} password - Contraseña del usuario
   * @returns {Promise<void>}
   * @throws {Error} Si las credenciales son inválidas
   */
  const login = async (email, password) => {
    try {
      const response = await authService.login(email, password)
      
      // Guardar token y usuario en localStorage
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      
      // Actualizar estado
      setToken(response.token)
      setUser(response.user)
      
      return response
    } catch (error) {
      throw error
    }
  }

  /**
   * Cierra la sesión del usuario.
   */
  const logout = () => {
    // Limpiar localStorage
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    
    // Limpiar estado
    setToken(null)
    setUser(null)
  }

  /**
   * Verifica si el usuario está autenticado.
   * 
   * @returns {boolean} true si el usuario está autenticado
   */
  const isAuthenticated = () => {
    return !!token && !!user
  }

  /**
   * Verifica si el usuario tiene un rol específico.
   * 
   * @param {string} role - Rol a verificar (ADMIN, CUSTOMER, SELLER)
   * @returns {boolean} true si el usuario tiene el rol
   */
  const hasRole = (role) => {
    return user?.role === role
  }

  /**
   * Verifica si el usuario es administrador.
   * 
   * @returns {boolean} true si el usuario es administrador
   */
  const isAdmin = () => {
    return hasRole('ADMIN')
  }

  const value = {
    user,
    token,
    loading,
    login,
    logout,
    isAuthenticated: isAuthenticated(),
    hasRole,
    isAdmin: isAdmin()
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export default AuthContext

