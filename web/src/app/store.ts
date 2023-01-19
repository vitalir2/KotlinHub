import {configureStore} from '@reduxjs/toolkit';
import {createAppGraph} from "./dependency_injection";
import {repositoriesSlice} from "../features/main/redux/slice";
import {loginSlice} from "../features/login/LoginSlice";

export const store = configureStore({
  reducer: {
      repositories: repositoriesSlice.reducer,
      login: loginSlice.reducer,
  },
  middleware: getDefaultMiddleware =>
      getDefaultMiddleware({
        thunk: {
          extraArgument: {
              appGraph: createAppGraph(),
          }
        }
      })
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
