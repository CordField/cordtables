export async function fetchAs<REQ, RES>(path: string, data: REQ) {
  const result = await fetch(process.env.SERVER_URL + path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'Accept': 'application/json',
    },
    body: JSON.stringify(data),
  });

  const json = await result.json();

  return json as unknown as RES;
}

export const capitalize = (str: string) => {
  return str.replace(/^\w/, (c) => c.toUpperCase());
}