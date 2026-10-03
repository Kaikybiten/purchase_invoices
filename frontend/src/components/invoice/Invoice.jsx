import React from "react";
import ProductItem from "./ProductItem";

function Invoice({ data }) {
    return (
        <div>
            {data.map((element) => (
                <ProductItem key={element.id} item={element} />
            ))}
        </div>
    );
}

export default Invoice;
