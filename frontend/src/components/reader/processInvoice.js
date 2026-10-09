import { postInvoices } from "../../service/invoices";

export async function processInvoice(url, setQrCode, setData) {
    setQrCode(url);

    const products = await postInvoices(url);

    if (products) {
        setData(products);
    }
}
