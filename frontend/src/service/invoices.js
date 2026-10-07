export async function postInvoices(url) {
    const response = await fetch("/invoices", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
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

    return jsonResponse.products;
}
