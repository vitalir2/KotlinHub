import {configureStore} from '@reduxjs/toolkit';
import {createAppGraph} from "./dependency_injection";
import {repositoriesSlice} from "../features/main/redux/slice";

export const store = configureStore({
  reducer: {
      repositories: repositoriesSlice.reducer,
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
