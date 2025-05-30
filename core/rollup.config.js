import commonjs from '@rollup/plugin-commonjs';
import resolve from '@rollup/plugin-node-resolve';
import { terser } from 'rollup-plugin-terser';

export default [
  {
    input: 'src/main/resources/static/form-keeper.js',
    output: {
      file: 'src/main/resources/static/form-keeper.umd.js',
      format: 'umd',
      name: 'FormKeeper',
      sourcemap: true,
    },
    plugins: [
      resolve(),
      commonjs(),
    ],
  },
  {
    input: 'src/main/resources/static/form-keeper.js',
    output: {
      file: 'src/main/resources/static/form-keeper.umd.min.js',
      format: 'umd',
      name: 'FormKeeper',
      sourcemap: true,
    },
    plugins: [
      resolve(),
      commonjs(),
      terser({
        compress: true,
        mangle: true,
        format: {
          comments: false,
        },
        toplevel: true,
      }),
    ],
  }
];
