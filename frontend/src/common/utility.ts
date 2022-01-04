type fetchAs = <REQ, RES>(path: string, data: REQ) => Promise<RES>;

export function throttle<REQ, RES>(fn: fetchAs, delay) {
  let last = 0;
  return (...args) => {
    const now = new Date().getTime();
    if (now - last < delay) {
      return;
    }
    last = now; //so that for next execution
    return fn(args[0], args[1]);
  };
}

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

export const throttledFetchAs = throttle(fetchAs, 500000) as fetchAs;

export const capitalize = (str: string) => {
  return str.replace(/^\w/, c => c.toUpperCase());
};

export const capitalizePhrase = (str: string) => {
  return str.replace(/\w\S*/g, w => w.replace(/^\w/, c => c.toUpperCase()));
};
