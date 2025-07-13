# Git Branch Naming Guidelines for Manage Opentalk Website

## ✅ General Format

```
<type>/<role>-<module>-<short-description>
```

- `type`: feature, fix, refactor, hotfix, test, docs, release
- `role`: api (backend), screen (frontend)
- `module`: functional module (auth, employee, opentalk, etc.)
- `short-description`: kebab-case, English, concise

---

## 📌 Suggested Branch Names by Feature

| Feature                       | Backend Branch Name                          | Frontend Branch Name                      |
| ----------------------------- | -------------------------------------------- | ----------------------------------------- |
| 🔐 Authentication             | feature/api-auth-login-jwt                   | feature/screen-auth-login-screen          |
| 👥 Employee Management        | feature/api-employee-sync-hrm                | feature/screen-employee-list-filter       |
| 🗓 Opentalk Management         | feature/api-opentalk-create                  | feature/screen-opentalk-register-form     |
| 🖼 Slide Upload                | feature/api-slide-upload-handler             | feature/screen-slide-upload-ui            |
| 🎰 Opentalk Lottery           | feature/api-lottery-random-host              | feature/screen-lottery-notification-modal |
| ⏰ Dynamic Cronjob Config     | feature/api-cronjob-lottery-config           | _(Not applicable)_                        |
| 📄 Employee Filters           | feature/api-employee-filter-by-branch-status | feature/screen-employee-advanced-filters  |
| 🏢 Branch Management          | feature/api-branch-management-api            | feature/screen-branch-select-dropdown     |
| 🧾 Opentalk Topics Management | feature/api-topic-create-edit-delete         | feature/screen-topic-input-form           |
| 📊 Opentalk Session Listing   | feature/api-opentalk-pagination-filters      | feature/screen-opentalk-list-table        |
| 📧 Email Notification         | feature/api-email-thymeleaf-reminder         | _(Not applicable)_                        |
| 🌐 Internationalization       | feature/api-i18n-config-support              | feature/screen-i18n-language-toggle       |
| 🔍 Query Optimization         | refactor/api-query-cache-pagination          | _(Not applicable)_                        |
| 🧪 Testing                    | test/api-opentalk-controller-test            | test/screen-opentalk-screen-test          |
| 🚀 Release                    | release/api-v1.0.0                           | release/screen-v1.0.0                     |

---

## ✏️ Commit Message Format

```
<type> - <role>: <short-description>
```

### Examples:

- `feat - api: implement JWT login endpoint`
- `fix - api: correct HRM sync bug`
- `refactor - api: optimize schedule fetching logic`
- `test - api: add unit test for upload controller`

### Conventional Commit Types:

- `feat`: new feature
- `fix`: bug fix
- `refactor`: code restructuring
- `test`: test related
- `docs`: documentation update
- `chore`: minor changes not affecting logic

---

## 🔀 Pull Request Naming Template

```
[type] [module] - short description
```

### Examples:

- `[Feature] Auth - JWT login API`
- `[Fix] Employee - Sync error on HRM import`
- `[Refactor] Opentalk - Clean up service layer`
- `[Test] Slide - Unit test for upload`

👉 **Tips:**

- Always link to related ticket/task (JIRA/GitHub issue).
- Use checklists in PR description to review key items.

---

## 🛠 Naming Tips

- Use lowercase letters and `-` to separate words.
- Keep it short but descriptive enough to identify the task.
- Include Jira/task ID if your workflow uses it:

  - Example: `feature/api-opentalk-create-JIRA123`

---

## 🧠 Why Follow This?

- Easier to track features and ownership.
- Clear separation of backend vs frontend responsibilities.
- Cleaner Git history and collaboration.

---

Happy branching! 🌿
