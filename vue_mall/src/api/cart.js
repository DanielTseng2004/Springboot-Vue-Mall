export const cartUtil = {
  // 獲取購物車內容
  getCart() {
    return JSON.parse(localStorage.getItem("cart")) || [];
  },

  // 加入購物車
  addToCart(product, quantity = 1) {
    const cart = this.getCart();
    const index = cart.findIndex(
      (item) => item.productId === product.productId,
    );

    if (index > -1) {
      cart[index].quantity += quantity;
    } else {
      cart.push({
        productId: product.productId,
        productName: product.productName,
        price: product.price,
        imageUrl: product.imageUrl,
        quantity: quantity,
      });
    }
    localStorage.setItem("cart", JSON.stringify(cart));
    // 觸發自定義事件通知其他組件（如 Navbar）更新
    window.dispatchEvent(new Event("cart-updated"));
  },

  // 從購物車移除
  removeFromCart(productId) {
    const cart = this.getCart().filter((item) => item.productId !== productId);
    localStorage.setItem("cart", JSON.stringify(cart));
    window.dispatchEvent(new Event("cart-updated"));
  },

  // 清空購物車
  clearCart() {
    localStorage.removeItem("cart");
    window.dispatchEvent(new Event("cart-updated"));
  },
};
