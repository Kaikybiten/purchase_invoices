export interface Product {
    code: string;
    name: string;
    measure: string;
    unitPrice: number;
    totalPrice: number;
    quantity: number;
}

export interface RecordedInvoice {
    accessToken: string;
    invoiceEntryDate: Date;
    products: Product[];
}
