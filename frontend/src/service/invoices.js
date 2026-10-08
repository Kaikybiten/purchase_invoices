const API_KEY = import.meta.env.VITE_API_KEY;

export async function postInvoices(url) {
    if (!API_KEY) {
        console.error("VITE_API_KEY não está configurada.");
        return;
    }

    const response = await fetch("/invoices", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-API-Key": API_KEY,
        },
        body: JSON.stringify({ url }),
    });

    if (!response.ok) {
        console.log(`Erro HTTP: ${response.status}`);
        return;
    }

    const jsonResponse = await response.json();

    if (!jsonResponse.success) {
        console.log(jsonResponse.message);
        return;
    }

    return jsonResponse.data;
}
