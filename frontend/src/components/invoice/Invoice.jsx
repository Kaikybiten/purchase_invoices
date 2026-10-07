import React from "react";
import ProductItem from "./ProductItem";

function Invoice({ data }) {
    return (
        <div className="space-y-2">
            {data.map((element) => (
                <div key={element.code}>
                    <ProductItem item={element} />
                    <hr className="border-dashed border-gray-300" />
                </div>
            ))}
        </div>
    );
}

export default Invoice;
