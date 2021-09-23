export async function fetchAs<REQ, RES>(path: string, data: REQ) {
  const result = await fetch(process.env.SERVER_URL + path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'Accept': 'application/json;charset=utf-8',
    },
    body: JSON.stringify(data),
  });

  console.log(result);

  const json = await result.json();

  console.log(json);

  return json as unknown as RES;
}
