import { Dispatch } from "react";
import { postInvoices } from "../../service/invoices";
import { RecordedInvoice } from "../../types/invoice";

export async function processInvoice(
    url: string,
    setQrCode: Dispatch<string>,
    setData: Dispatch<RecordedInvoice>,
) {
    setQrCode(url);

    const products = await postInvoices(url);

    if (products) {
        setData(products);
    }
}
