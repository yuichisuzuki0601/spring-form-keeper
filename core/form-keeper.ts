const MODULE_NAME = "form-keeper";

type Data = Record<
  string,
  string | string[] | number | number[] | boolean | boolean[]
>;

type Option = {
  delay?: number;
  beforeRestoreMessage?: string;
  onRestore?: (data: Data) => void;
};

const elementHandlers = new Map<Element, EventListener>();
const debounceTimers = new Map<string, ReturnType<typeof setTimeout>>();

export async function observe(
  id: string | number,
  viewName: string,
  option?: Option
) {
  unobserve();

  console.log(`[${MODULE_NAME}] observe start id=${id}, view=${viewName}`);

  option = {
    delay: 3000,
    beforeRestoreMessage:
      "Unsaved input detected. Would you like to restore it?",
    ...(option ?? {}),
  };

  document.querySelectorAll("input, textarea, select").forEach((element) => {
    const el = element as
      | HTMLInputElement
      | HTMLTextAreaElement
      | HTMLSelectElement;

    const handler = async () => {
      const { name } = el;

      if (!name) return;

      const oldTimer = debounceTimers.get(name);
      if (oldTimer !== undefined) {
        clearTimeout(oldTimer);
      }

      const timeoutId = window.setTimeout(async () => {
        let value = el.value;

        // checkbox
        if (el.type === "checkbox") {
          const checkboxes = document.querySelectorAll<HTMLInputElement>(
            `input[type="checkbox"][name="${name}"]`
          );
          value = Array.from(checkboxes)
            .filter((cb) => cb.checked)
            .map((cb) => cb.value)
            .join();
        }

        console.log(`[${MODULE_NAME}] ${name}: ${value}`);

        // TODO 一旦目的遂行のためにローカルストレージは使わない
        // 古い端末側で古い保存データが読まれてしまうため
        // const data = JSON.parse(localStorage.getItem(MODULE_NAME) ?? "{}");
        // data[viewName] = data[viewName] ?? {};
        // data[viewName][name] = value;
        // localStorage.setItem(MODULE_NAME, JSON.stringify(data));

        const res = await fetch("/form-keeper", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: String(id),
            viewName,
            name,
            value,
          }),
        });
        if (!res.ok) {
          console.error(`[${MODULE_NAME}] post failed`);
        }
        debounceTimers.delete(name);
        // TODO 「autosaved」ってSnackbar出せたらかっこいい
      }, option.delay);
      debounceTimers.set(name, timeoutId);
    };

    if (
      element.tagName.toLowerCase() === "textarea" ||
      (element instanceof HTMLInputElement &&
        (element.type === "text" ||
          element.type === "email" ||
          element.type === "password" ||
          element.type === "number" ||
          element.type === "url" ||
          element.type === "search" ||
          element.type === "tel"))
    ) {
      element.addEventListener("input", handler);
    } else {
      element.addEventListener("change", handler);
    }
    elementHandlers.set(element, handler);
  });

  let data = JSON.parse(localStorage.getItem(MODULE_NAME) ?? "{}");
  data[viewName] = data[viewName] ?? {};

  const res = await fetch(
    `/form-keeper?${new URLSearchParams({
      id: String(id),
      viewName,
    }).toString()}`
  );
  if (res.ok) {
    data[viewName] = { ...data[viewName], ...(await res.json()) };
  }
  if (Object.keys(data[viewName]).length) {
    if (confirm(option.beforeRestoreMessage)) {
      const restoreData = data[viewName] as Data;
      if (option.onRestore) {
        option.onRestore(restoreData);
      } else {
        Object.entries(restoreData).forEach(([name, value]) => {
          // checkbox
          const checkboxElements = document.querySelectorAll<HTMLInputElement>(
            `input[type="checkbox"][name="${name}"]`
          );
          if (checkboxElements.length > 0) {
            const values = String(value).split(",");
            checkboxElements.forEach((el) => {
              el.checked = values.includes(el.value);
            });
            return;
          }
          // radio
          const radioElements = document.querySelectorAll<HTMLInputElement>(
            `input[type="radio"][name="${name}"]`
          );
          if (radioElements.length > 0) {
            radioElements.forEach((el) => {
              el.checked = el.value === String(value);
            });
            return;
          }
          // else
          const element = document.querySelector(`[name="${name}"]`);
          if (element instanceof HTMLInputElement) {
            element.value = String(value);
          }
          if (element instanceof HTMLTextAreaElement) {
            element.value = String(value);
          }
          if (element instanceof HTMLSelectElement) {
            element.value = String(value);
          }
        });
      }
    }
  }
  await clear(id, viewName);
}

export async function clear(id: string | number, viewName: string) {
  const data = JSON.parse(localStorage.getItem(MODULE_NAME) ?? "{}");
  if (data[viewName]) {
    delete data[viewName];
    localStorage.setItem(MODULE_NAME, JSON.stringify(data));
  }
  await fetch(
    `/form-keeper?${new URLSearchParams({
      id: String(id),
      viewName,
    }).toString()}`,
    {
      method: "DELETE",
    }
  );
  console.log(
    `[${MODULE_NAME}] cleared data for id=${id}, viewName=${viewName}`
  );
}

export function unobserve() {
  elementHandlers.forEach((handler, element) => {
    element.removeEventListener("input", handler);
    element.removeEventListener("change", handler);
  });
  elementHandlers.clear();

  console.log(`[${MODULE_NAME}] observe end`);
}
