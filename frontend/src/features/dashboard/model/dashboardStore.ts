import { create } from 'zustand';
import { DATE_RANGE_DAYS } from '@/shared/config/constants';

type DashboardState = {
  selectedRangeDays: number;
  setSelectedRangeDays: (selectedRangeDays: number) => void;
};

export const useDashboardStore = create<DashboardState>((set) => ({
  selectedRangeDays: DATE_RANGE_DAYS,
  setSelectedRangeDays: (selectedRangeDays) => set({ selectedRangeDays }),
}));
