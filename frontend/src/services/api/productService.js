import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

/**
 * Servicio para interactuar con la API de productos.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
class ProductService {
  /**
   * Obtiene el header de autorización con el token JWT.
   * 
   * @param {string} token - Token JWT
   * @returns {Object} Headers con autorización
   */
  getAuthHeaders(token) {
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  }

  /**
   * Obtiene todos los productos del catálogo.
   * 
   * @param {string} token - Token JWT (opcional, para endpoints protegidos)
   * @returns {Promise<Array>} Lista de productos
   * @throws {Error} Si hay un error en la petición
   */
  async getProducts(token = null) {
    try {
      const config = token ? { headers: this.getAuthHeaders(token) } : {}
      const response = await axios.get(`${API_BASE_URL}/products`, config)
      return response.data
    } catch (error) {
      console.error('Error al obtener productos:', error)
      throw new Error(
        error.response?.data?.message || 
        'Error al cargar los productos. Por favor, intenta nuevamente.'
      )
    }
  }

  /**
   * Obtiene un producto por su ID.
   * 
   * @param {number} productId - ID del producto
   * @param {string} token - Token JWT (opcional)
   * @returns {Promise<Object>} Datos del producto
   * @throws {Error} Si hay un error en la petición o el producto no existe
   */
  async getProductById(productId, token = null) {
    try {
      const config = token ? { headers: this.getAuthHeaders(token) } : {}
      const response = await axios.get(`${API_BASE_URL}/products/${productId}`, config)
      return response.data
    } catch (error) {
      console.error('Error al obtener producto:', error)
      if (error.response?.status === 404) {
        throw new Error('Producto no encontrado')
      }
      throw new Error(
        error.response?.data?.message || 
        'Error al cargar el producto. Por favor, intenta nuevamente.'
      )
    }
  }

  /**
   * Obtiene productos filtrados por categoría.
   * 
   * @param {string} category - Categoría a filtrar
   * @param {string} token - Token JWT (opcional)
   * @returns {Promise<Array>} Lista de productos de la categoría
   * @throws {Error} Si hay un error en la petición
   */
  async getProductsByCategory(category, token = null) {
    try {
      const config = token ? { headers: this.getAuthHeaders(token) } : {}
      const response = await axios.get(
        `${API_BASE_URL}/products/search?category=${encodeURIComponent(category)}`,
        config
      )
      return response.data
    } catch (error) {
      console.error('Error al obtener productos por categoría:', error)
      throw new Error(
        error.response?.data?.message || 
        'Error al cargar los productos. Por favor, intenta nuevamente.'
      )
    }
  }

  /**
   * Crea un nuevo producto.
   * 
   * @param {Object} productData - Datos del producto
   * @param {string} productData.name - Nombre del producto
   * @param {string} productData.description - Descripción del producto
   * @param {number} productData.price - Precio del producto
   * @param {number} productData.stock - Stock disponible
   * @param {string} productData.category - Categoría del producto
   * @param {string} token - Token JWT (requerido)
   * @returns {Promise<Object>} Producto creado
   * @throws {Error} Si hay un error en la petición
   */
  async createProduct(productData, token) {
    try {
      if (!token) {
        throw new Error('Token de autenticación requerido')
      }

      const response = await axios.post(
        `${API_BASE_URL}/products`,
        productData,
        { headers: this.getAuthHeaders(token) }
      )
      return response.data
    } catch (error) {
      console.error('Error al crear producto:', error)
      if (error.response?.status === 401) {
        throw new Error('No autorizado. Por favor inicia sesión.')
      }
      throw new Error(
        error.response?.data?.message || 
        'Error al crear el producto. Por favor, intenta nuevamente.'
      )
    }
  }

  /**
   * Actualiza un producto existente.
   * 
   * @param {number} productId - ID del producto a actualizar
   * @param {Object} productData - Nuevos datos del producto
   * @param {string} token - Token JWT (requerido)
   * @returns {Promise<Object>} Producto actualizado
   * @throws {Error} Si hay un error en la petición
   */
  async updateProduct(productId, productData, token) {
    try {
      if (!token) {
        throw new Error('Token de autenticación requerido')
      }

      const response = await axios.put(
        `${API_BASE_URL}/products/${productId}`,
        productData,
        { headers: this.getAuthHeaders(token) }
      )
      return response.data
    } catch (error) {
      console.error('Error al actualizar producto:', error)
      if (error.response?.status === 401) {
        throw new Error('No autorizado. Por favor inicia sesión.')
      }
      if (error.response?.status === 404) {
        throw new Error('Producto no encontrado')
      }
      throw new Error(
        error.response?.data?.message || 
        'Error al actualizar el producto. Por favor, intenta nuevamente.'
      )
    }
  }

  /**
   * Elimina un producto.
   * 
   * @param {number} productId - ID del producto a eliminar
   * @param {string} token - Token JWT (requerido)
   * @returns {Promise<void>}
   * @throws {Error} Si hay un error en la petición
   */
  async deleteProduct(productId, token) {
    try {
      if (!token) {
        throw new Error('Token de autenticación requerido')
      }

      await axios.delete(
        `${API_BASE_URL}/products/${productId}`,
        { headers: this.getAuthHeaders(token) }
      )
    } catch (error) {
      console.error('Error al eliminar producto:', error)
      if (error.response?.status === 401) {
        throw new Error('No autorizado. Por favor inicia sesión.')
      }
      if (error.response?.status === 404) {
        throw new Error('Producto no encontrado')
      }
      throw new Error(
        error.response?.data?.message || 
        'Error al eliminar el producto. Por favor, intenta nuevamente.'
      )
    }
  }
}

// Exportar una instancia única del servicio (Singleton)
export default new ProductService()

