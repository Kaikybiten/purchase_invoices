import React from "react";
import ProductItem from "./ProductItem";

function Invoice({ data }) {
    const base = {
        accessToken: "35260971779813001818651080001114361002221145",
        invoiceEntryDate: "2026-09-18",
        products: [
            {
                code: "7908490900004",
                name: "FEIJAO X TP1 CARI 1K",
                measure: "UN",
                unitPrice: 7.49,
                totalPrice: 7.49,
                quantity: 1,
            },
            {
                code: "3692",
                name: "FILE FRANGO BDJ KG",
                measure: "KG",
                unitPrice: 31.19,
                totalPrice: 14.41,
                quantity: 0.462,
            },
            {
                code: "1710",
                name: "ALHO TRADIC KG",
                measure: "KG",
                unitPrice: 29.9,
                totalPrice: 3.74,
                quantity: 0.125,
            },
            {
                code: "7894904571956",
                name: "MARG DORIA C/S 500G",
                measure: "UN",
                unitPrice: 5.99,
                totalPrice: 5.99,
                quantity: 1,
            },
            {
                code: "3836",
                name: "CEBOLA KG",
                measure: "KG",
                unitPrice: 7.99,
                totalPrice: 3.56,
                quantity: 0.445,
            },
            {
                code: "600",
                name: "MIOLO ACEM KG (D)",
                measure: "KG",
                unitPrice: 39.9,
                totalPrice: 25.86,
                quantity: 0.648,
            },
            {
                code: "3762",
                name: "COXAO MOLE PECA KG",
                measure: "KG",
                unitPrice: 49.99,
                totalPrice: 42.69,
                quantity: 0.854,
            },
        ],
    };

    const products = base.products;

    return (
        <div className="space-y-2">
            {products.map((element) => (
                <div key={element.code}>
                    <ProductItem item={element} />
                    <hr className="border-dashed border-gray-300" />
                </div>
            ))}
        </div>
    );
}

export default Invoice;
