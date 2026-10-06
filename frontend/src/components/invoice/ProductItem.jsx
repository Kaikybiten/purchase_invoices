import React from "react";

function ProductItem({ item }) {
    return (
        <div className="flex items-center justify-between gap-4 mb-2">
            <div className="min-w-0">
                <p className="font-semibold">{item.name}</p>

                <p className="mt-1 text-xs text-gray-500">
                    {`${item.quantity} ${item.measure}`}
                </p>
            </div>

            <span className="text-sm">{`R$ ${String(item.totalPrice).replace(".", ",")}`}</span>
        </div>
    );
}

export default ProductItem;
