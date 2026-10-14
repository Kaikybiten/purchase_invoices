export async function postInvoices(url) {
    // Cria sessão anônima antes de enviar a nota fiscal
    const sessionResponse = await fetch("/session", {
        method: "POST",
        credentials: "same-origin",
    });

    if (!sessionResponse.ok) {
        console.log(`Erro ao criar sessão: ${sessionResponse.status}`);
        return;
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
        return;
    }

    const jsonResponse = await response.json();

    console.log(jsonResponse);

    if (!jsonResponse.success) {
        console.log(jsonResponse.message);
        return;
    }

    return jsonResponse.data;
}
