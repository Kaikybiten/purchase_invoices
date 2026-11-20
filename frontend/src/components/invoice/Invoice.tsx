import ProductItem from "./ProductItem";

import { Product, RecordedInvoice } from "../../types/invoice";

function Invoice({ data }: { data: RecordedInvoice }) {
    return (
        <div className="space-y-2">
            {data.products.map((element: Product) => (
                <div key={element.code}>
                    <ProductItem item={element} />
                    <hr className="border-dashed border-gray-300" />
                </div>
            ))}
        </div>
    );
}

export default Invoice;
