import { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import productService from '../services/api/productService'
import './ProductDetail.css'

/**
 * Página de detalle de un producto.
 * Muestra toda la información del producto seleccionado.
 * 
 * @returns {JSX.Element} Página de detalle de producto
 */
function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchProduct()
  }, [id])

  /**
   * Obtiene los detalles del producto desde la API.
   */
  const fetchProduct = async () => {
    try {
      setLoading(true)
      setError('')
      const productData = await productService.getProductById(id)
      setProduct(productData)
    } catch (err) {
      setError(err.message || 'Error al cargar el producto')
    } finally {
      setLoading(false)
    }
  }

  const formatPrice = (price) => {
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'EUR'
    }).format(price)
  }

  const getStockStatus = (stock) => {
    if (stock === 0) {
      return { 
        text: 'Sin stock', 
        className: 'stock-out',
        message: 'Este producto no está disponible actualmente'
      }
    }
    if (stock < 5) {
      return { 
        text: 'Últimas unidades', 
        className: 'stock-low',
        message: `Solo quedan ${stock} unidades disponibles`
      }
    }
    return { 
      text: 'Disponible', 
      className: 'stock-available',
      message: `${stock} unidades disponibles`
    }
  }

  if (loading) {
    return (
      <div className="product-detail-page">
        <div className="container">
          <div className="product-detail-loading">
            <div className="loading-spinner"></div>
            <p>Cargando producto...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error || !product) {
    return (
      <div className="product-detail-page">
        <div className="container">
          <div className="product-detail-error">
            <div className="error-icon">⚠️</div>
            <h2>Error al cargar el producto</h2>
            <p>{error || 'El producto no existe o no está disponible'}</p>
            <Link to="/products" className="btn-back">
              Volver al catálogo
            </Link>
          </div>
        </div>
      </div>
    )
  }

  const stockStatus = getStockStatus(product.stock)

  return (
    <div className="product-detail-page">
      <div className="container">
        <nav className="breadcrumb">
          <Link to="/">Inicio</Link>
          <span>/</span>
          <Link to="/products">Productos</Link>
          <span>/</span>
          <span>{product.name}</span>
        </nav>

        <div className="product-detail">
          <div className="product-detail-image">
            <div className="product-image-large">
              <span className="product-placeholder-large">🛍️</span>
              {product.stock === 0 && (
                <div className="product-badge-large">
                  Agotado
                </div>
              )}
            </div>
          </div>

          <div className="product-detail-info">
            <div className="product-header-detail">
              <span className="product-category-badge">{product.category}</span>
              <h1 className="product-title">{product.name}</h1>
            </div>

            <div className="product-price-detail">
              <span className="price-label">Precio:</span>
              <span className="price-value">{formatPrice(product.price)}</span>
            </div>

            {product.description && (
              <div className="product-description-detail">
                <h3>Descripción</h3>
                <p>{product.description}</p>
              </div>
            )}

            <div className="product-stock-detail">
              <h3>Disponibilidad</h3>
              <div className={`stock-info ${stockStatus.className}`}>
                <span className="stock-label">{stockStatus.text}</span>
                <span className="stock-message">{stockStatus.message}</span>
              </div>
            </div>

            <div className="product-actions">
              {product.stock > 0 ? (
                <button className="btn-add-to-cart">
                  Agregar al Carrito
                </button>
              ) : (
                <button className="btn-add-to-cart" disabled>
                  Producto Agotado
                </button>
              )}
              <button 
                onClick={() => navigate(-1)}
                className="btn-back-detail"
              >
                Volver
              </button>
            </div>

            <div className="product-meta">
              <div className="meta-item">
                <strong>ID:</strong> {product.id}
              </div>
              <div className="meta-item">
                <strong>Categoría:</strong> {product.category}
              </div>
              {product.createdAt && (
                <div className="meta-item">
                  <strong>Fecha de registro:</strong>{' '}
                  {new Date(product.createdAt).toLocaleDateString('es-ES', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ProductDetail

