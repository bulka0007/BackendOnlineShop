package com.bulka.OnlineShop.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    @Transactional
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    @Transactional
    fun updateProduct(id: Long, product: Product): Product? {
        return productRepository.findById(id).map { existingProduct ->
            existingProduct.name = product.name
            existingProduct.description = product.description
            existingProduct.price = product.price
            existingProduct.stock = product.stock
            productRepository.save(existingProduct)
        }.orElse(null)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }
}