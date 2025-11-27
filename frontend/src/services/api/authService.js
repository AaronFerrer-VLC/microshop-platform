import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

/**
 * Servicio para interactuar con la API de autenticación.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
class AuthService {
  /**
   * Realiza el login del usuario.
   * 
   * @param {string} email - Email del usuario
   * @param {string} password - Contraseña del usuario
   * @returns {Promise<Object>} Objeto con token y datos del usuario
   * @throws {Error} Si las credenciales son inválidas
   */
  async login(email, password) {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, {
        email,
        password
      })
      return response.data
    } catch (error) {
      console.error('Error al iniciar sesión:', error)
      throw new Error(
        error.response?.data?.message || 
        'Error al iniciar sesión. Verifica tus credenciales.'
      )
    }
  }

  /**
   * Registra un nuevo usuario.
   * 
   * @param {Object} userData - Datos del usuario a registrar
   * @param {string} userData.name - Nombre del usuario
   * @param {string} userData.email - Email del usuario
   * @param {string} userData.password - Contraseña del usuario
   * @param {string} userData.role - Rol del usuario (opcional, por defecto CUSTOMER)
   * @returns {Promise<Object>} Datos del usuario registrado
   * @throws {Error} Si hay un error en el registro
   */
  async register(userData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/users`, userData)
      return response.data
    } catch (error) {
      console.error('Error al registrar usuario:', error)
      throw new Error(
        error.response?.data?.message || 
        'Error al registrar usuario. Intenta nuevamente.'
      )
    }
  }

  /**
   * Verifica si un token es válido haciendo una petición al servidor.
   * 
   * @param {string} token - Token JWT
   * @returns {Promise<boolean>} true si el token es válido
   */
  async validateToken(token) {
    try {
      // Hacer una petición a un endpoint protegido para validar el token
      const response = await axios.get(`${API_BASE_URL}/users`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      return response.status === 200
    } catch (error) {
      return false
    }
  }
}

// Exportar una instancia única del servicio (Singleton)
export default new AuthService()

