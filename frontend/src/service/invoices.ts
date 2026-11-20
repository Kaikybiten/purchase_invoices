import { RecordedInvoice } from "../types/invoice.ts";

export async function postInvoices(
    url: string,
): Promise<RecordedInvoice | null> {
    // Cria sessão anônima antes de enviar a nota fiscal
    const sessionResponse = await fetch("/session", {
        method: "POST",
        credentials: "same-origin",
    });

    if (!sessionResponse.ok) {
        console.log(`Erro ao criar sessão: ${sessionResponse.status}`);
        return null;
    }

    // Envia a nota fiscal
    const response = await fetch("/invoices", {
        method: "POST",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ url }),
    });

    if (!response.ok) {
        console.log(`Erro HTTP: ${response.status}`);
        console.log(await response.text());
        return null;
    }

    const jsonResponse = await response.json();

    if (!jsonResponse.success) {
        console.log(jsonResponse.message);
        return null;
    }

    return jsonResponse.data;
}
