import {configureStore, ThunkAction, Action, applyMiddleware} from '@reduxjs/toolkit';
import {createAppGraph} from "./dependency_injection";
import {repositoriesSlice} from "../features/main/redux/repositoriesSlice";

export const store = configureStore({
  reducer: {
      repositories: repositoriesSlice.reducer,
  },
  middleware: getDefaultMiddleware =>
      getDefaultMiddleware({
        thunk: {
          extraArgument: createAppGraph()
        }
      })
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
