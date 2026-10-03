import React from "react";

function ProductItem({ item }) {
    const product = item.product;

    return (
        <div>
            <strong>{product.name}</strong>{" "}
            <span>{`${item.quantity}x - ${item.totalPrice}`}</span>
        </div>
    );
}

export default ProductItem;
