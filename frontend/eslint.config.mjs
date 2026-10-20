import { dirname } from "path";
import { fileURLToPath } from "url";
import { FlatCompat } from "@eslint/eslintrc";
import eslintConfigPrettier from "eslint-config-prettier";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const compat = new FlatCompat({ baseDirectory: __dirname });

export default [
  ...compat.extends("next/core-web-vitals", "next/typescript", "airbnb"),
  eslintConfigPrettier,
  {
    files: ["eslint.config.mjs"],
    rules: {
      "import/no-extraneous-dependencies": ["error", { devDependencies: true }],
      "no-underscore-dangle": ["error", { allow: ["__filename", "__dirname"] }],
    },
  },
  {
    files: ["**/*.ts", "**/*.tsx"],
    rules: {
      "no-unused-vars": "off",
      "@typescript-eslint/no-unused-vars": ["error", { argsIgnorePattern: "^_" }],
    },
  },
  {
    settings: {
      "import/resolver": {
        typescript: { alwaysTryTypes: true },
      },
    },
    rules: {
      "react/function-component-definition": ["error", { namedComponents: "arrow-function" }],
      "react/jsx-filename-extension": ["error", { extensions: [".tsx"] }],
      "react/react-in-jsx-scope": "off",
      "react/require-default-props": "off",
      "import/extensions": ["error", "ignorePackages", { ts: "never", tsx: "never" }],
      "import/prefer-default-export": "off",
      "no-use-before-define": ["error", { functions: false, classes: true }],
    },
  },
  {
    ignores: [
      "node_modules/**",
      ".next/**",
      "out/**",
      "build/**",
      "next-env.d.ts",
      "src/components/ui/**",
    ],
  },
];
