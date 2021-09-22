export async function fetchAs<REQ, RES>(path: string, data: REQ) {
  const result = await fetch('http://localhost:8080/' + path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
    body: JSON.stringify(data),
  });

  const json = await result.json();
  return json as unknown as RES;
}
